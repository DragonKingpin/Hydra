package com.walnut.sparta.ucdn.console.ufm;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ClusterPage;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.LocalCluster;
import com.pinecone.hydra.storage.file.transmit.exporter.TitanFileExportEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNService;
import com.walnut.sparta.ucdn.console.ufm.event.UFMEventListener;
import com.walnut.sparta.ucdn.console.ufm.protocol.RequestHead;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UOFSFileMultiDistributionService implements FileMultiDistributionService {
    protected KOMFileSystem                       primaryFileSystem;

    protected UniformVolumeManager                primaryVolume;

    protected SessionPhaser                       sessionPhaser;

    protected UlfBroadcastControlNode             transmitClient;

    protected BroadcastControlProducer            transmitProducer;

    protected BroadcastControlConsumer            transmitConsumer;

    protected List<UFMEventListener>              fileTransmitCompleteEventListeners;

    protected SessionValidator                    fileSessionValidator;

    protected UCDNService                         ucdnService;

    protected UFMConfig                           config;

    public UOFSFileMultiDistributionService( UCDNService ucdnService ) {
        this.ucdnService            = ucdnService;
        this.primaryFileSystem      = ucdnService.getKOMFileSystem();
        this.primaryVolume          = ucdnService.getUniformVolumeManager();
        this.sessionPhaser          = new UFMSessionPhaser();
        this.transmitClient         = ucdnService.getPrimaryMessageMiddlewareDirector().getPrimaryKafkaClient();
        this.config                 = ucdnService.getClusterFileSynchronizationConfig();
        this.fileSessionValidator   = new UFMSessionValidator( this );

        this.fileTransmitCompleteEventListeners = new ArrayList<>();
    }

    @Override
    public FileMultiDistributionService registerFileTransmitCompleteEventListener( UFMEventListener listener ) {
        if ( this.hasStarted() ) {
            throw new IllegalStateException( "FileMultiDistributionService has already started." );
        }

        this.fileTransmitCompleteEventListeners.add( listener );
        return this;
    }

    @Override
    public FileMultiDistributionService deregisterFileTransmitCompleteEventListener( UFMEventListener listener ) {
        if ( this.hasStarted() ) {
            throw new IllegalStateException( "FileMultiDistributionService has already started." );
        }

        this.fileTransmitCompleteEventListeners.remove( listener );
        return this;
    }


    @Override
    public boolean hasStarted() {
        return this.transmitProducer != null;
    }

    @Override
    public void start() throws UMBServiceException {
        if ( !this.hasStarted() ) {
            this.transmitProducer = this.transmitClient.createBroadcastControlProducer();
            this.transmitConsumer = this.transmitClient.createBroadcastControlConsumer( this.config.getFileCloudDistributeTransmitTopic(), this.config.getFileServiceTransmitGroup() );
            this.transmitConsumer.registerController( new FileMultiDistributionController( this ) );
            this.transmitConsumer.start();
            this.transmitProducer.start();

            if ( !this.fileSessionValidator.hasStarted() ) {
                this.fileSessionValidator.start();
            }
        }
    }

    @Override
    public void shutdown() {
        if ( this.hasStarted() ) {
            this.transmitConsumer.close();
            this.transmitProducer.close();
            this.transmitConsumer = null;
            this.transmitProducer = null;

            if ( this.fileSessionValidator.hasStarted() ) {
                this.fileSessionValidator.shutdown();
            }
        }
    }


    @Override
    public Collection<UFMEventListener > fetchFileTransmitCompleteEventListeners() {
        return this.fileTransmitCompleteEventListeners;
    }

    @Override
    public void fileDistribution( FileNode fileNode, String topic ) throws IOException, InterruptedException {
        this.sessionPhaser.registerFileLock( fileNode.getGuid(), new Object() );
        FSNodeAllotment fsNodeAllotment = this.primaryFileSystem.getFSNodeAllotment();
        FileMultiDistributionIface fileDistribution = this.transmitProducer.getIface(FileMultiDistributionIface.class, topic);
        String path = this.primaryFileSystem.getPath(fileNode.getGuid());


        long requestId = 0;
        RequestHead head = RequestHead.newRequest().setSessionId( System.currentTimeMillis() );
        fileDistribution.startDistribution( head, path, fileNode.getPhysicalSize() );
        //this.producer.issueInform( topic, "com.walnut.sparta.ucdn.console.umc.FileDistribution.startDistribution",path,fileNode.getPhysicalSize() );

        ClusterPage clusterPage = this.primaryFileSystem.fetchClustersByFileGuid( fileNode.getGuid() );

        long fileClusterNum = clusterPage.getClusters();

        int distributionFrameNum = 0;
        this.sessionPhaser.registerConsumerCount( fileNode.getGuid(),0L );

        for( long i = 0; i < fileClusterNum; ++i ){
            LocalCluster frame = clusterPage.getLocalCluster( i );
            // TODO, Remote
            UFMDClusterDO UFMDClusterDO = new UFMDClusterDO(
                    frame.getSourceName(), frame.getSize(), frame.getCrc32(),
                    path, i
            );

            fileDistribution.setFrameMeta( head, UFMDClusterDO );

            Path tempFilePath     = this.config.formatTemporaryPath( frame.getSegGuid().toString() );
            String szTempFilePath = tempFilePath.toString();
            File tempFile = new File( szTempFilePath );

            if ( !tempFile.createNewFile() ){
                throw new IOException( "Creating file compromised, what :" + szTempFilePath );
            }
            FileChannel channel = FileChannel.open( tempFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND );
            TitanFileChannelChanface kChannel = new TitanFileChannelChanface( channel );
            FileNode newFileNode = fsNodeAllotment.newFileNode();
            newFileNode.setPath( frame.getSourceName() );
            newFileNode.setDefinitionSize( frame.getSize() );
            TitanFileExportEntity64 exportEntity = new TitanFileExportEntity64( this.primaryFileSystem, this.primaryVolume, newFileNode, kChannel );
            exportEntity.export( frame );

            FileInputStream fileInputStream = new FileInputStream(tempFile);

//            int bufferSize = 950 * 1024;
//            byte[] buffer = new byte[ bufferSize ];
//            int bytesRead;
//
//            while( ( bytesRead = fileInputStream.read( buffer ) )!=-1 ) {
//                if ( bytesRead < bufferSize ) {
//                    byte[] validData = Arrays.copyOfRange(buffer, 0, bytesRead);
//                    buffer = validData;
//                }
//
//                fileDistribution.transmitClusterFrame( head, new UFMDClusterFrame( buffer, path, i, fileClusterNum ) );
//                //this.producer.issueInform( topic, "com.walnut.sparta.ucdn.console.umc.FileDistribution.transmitClusterFrame",new UFMDClusterFrame(buffer,path,i));
//            }

            int bufferSize = 2 * 1024 * 1024; // 2MB
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            int chunkSize = this.config.getFileFrameSize() * 1024;
            long currentPosition = 0;

            try {
                while ( (bytesRead = fileInputStream.read(buffer)) != -1 ) {
                    int chunksToProcess = (bytesRead + chunkSize - 1) / chunkSize; // 计算需要拆分的块数

                    for ( int j = 0; j < chunksToProcess; ++j ) {
                        // 计算当前块的起始和结束位置
                        int start = j * chunkSize;
                        int end = Math.min(start + chunkSize, bytesRead);

                        byte[] chunkData = Arrays.copyOfRange( buffer, start, end ); // 拆分出当前块

                        // 发送当前块的数据
                        fileDistribution.transmitClusterFrame(
                                head,
                                new UFMDClusterFrame(chunkData, path, i, fileClusterNum, currentPosition)
                        );
                        currentPosition = currentPosition + (end - start);
                    }
                }

                fileInputStream.close();
                if ( !tempFile.delete() ){
                    throw new IOException( "Purging temporary file compromised, what :" + szTempFilePath );
                }
            }
            finally {
                fileInputStream.close();
            }


            ++distributionFrameNum;
            if( distributionFrameNum == this.config.getBatchTransmitMemberThreshold() ){
                synchronized( this.sessionPhaser.getFileLock( fileNode.getGuid() ) ){
                    this.sessionPhaser.getFileLock( fileNode.getGuid() ).wait();
                }
                distributionFrameNum = 0;
            }
        }
    }

    @Override
    public void test() throws UMBServiceException {
        FileMultiDistributionIface fileDistribution = this.transmitProducer.getIface(FileMultiDistributionIface.class,"testTopic");
        FileNode fileNode = this.primaryFileSystem.getFileNode(GUIDs.GUID72("1214792-000373-0003-00"));
        String path = this.primaryFileSystem.getPath(fileNode.getGuid());
        //fileDistribution.startDistribution( path );
        BroadcastControlConsumer consumer = this.getTransmitConsumer("testTopic", "testGroup");
        consumer.start();
    }

    @Override
    public BroadcastControlConsumer getTransmitConsumer( String topic,String group ) {
        return this.transmitConsumer;
    }

    @Override
    public BroadcastControlProducer getTransmitProducer() {
        return this.transmitProducer;
    }
}
