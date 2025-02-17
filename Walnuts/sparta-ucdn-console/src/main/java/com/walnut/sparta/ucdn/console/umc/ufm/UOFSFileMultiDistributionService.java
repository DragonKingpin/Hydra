package com.walnut.sparta.ucdn.console.umc.ufm;

import com.pinecone.framework.util.Debug;
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
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

//@Component
public class UOFSFileMultiDistributionService implements FileMultiDistributionService {

//    @Resource
    private KOMFileSystem                   primaryFileSystem;

//    @Resource
    private SessionPhaser                   sessionPhaser;

//    @Resource
    private UniformVolumeManager            primaryVolume;

    protected UlfBroadcastControlNode       client;

    protected BroadcastControlProducer      producer;

    protected BroadcastControlConsumer      consumer;


//    public UCDNFileMultiDistribution(@Qualifier("kafkaFileServiceClient") UlfBroadcastControlNode client, FileDistributionController fileDistributionController ) throws UMBServiceException {
//        this.producer = client.createBroadcastControlProducer();
//        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNFileCloudDistributeTopic, UCDNConstants.UCDNFileServiceGroup);
//        consumer.registerController( fileDistributionController );
//        this.consumer.start();
//        this.producer.start();
//        this.client = client;
//    }

    public UOFSFileMultiDistributionService(MasterWarehouse masterWarehouse) throws UMBServiceException {
        this.primaryFileSystem = masterWarehouse.getKOMFileSystem();
        this.primaryVolume = masterWarehouse.getUniformVolumeManager();
        this.sessionPhaser = masterWarehouse.getSessionPhaser();
        this.client = masterWarehouse.getKafkaClient();
        this.producer = client.createBroadcastControlProducer();
        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNFileCloudDistributeTopic, UCDNConstants.UCDNFileServiceGroup);
        this.consumer.registerController( new UCDNFMDController( masterWarehouse ) );
        this.consumer.start();
        this.producer.start();
    }



    @Override
    public void fileDistribution( FileNode fileNode, String topic ) throws IOException, InterruptedException {
        this.sessionPhaser.registerFileLock( fileNode.getGuid(), new Object() );
        FSNodeAllotment fsNodeAllotment = this.primaryFileSystem.getFSNodeAllotment();
        FileMultiDistributionIface fileDistribution = this.producer.getIface(FileMultiDistributionIface.class, topic);
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
            //this.producer.issueInform( topic, "com.walnut.sparta.ucdn.console.umc.FileDistribution.setFrameMeta",frameVO );
            File tempFile = new File( UCDNConstants.FrameTempFilePath + frame.getSegGuid() + ".temp" );
            tempFile.createNewFile();
            FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
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
            long l = System.currentTimeMillis();
            int bufferSize = 2 * 1024 * 1024; // 2MB
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            int chunkSize = 950 * 1024; // 每次处理 900KB 的数据


            try {
                while ( (bytesRead = fileInputStream.read(buffer)) != -1 ) {
                    long fff = System.currentTimeMillis();
                    int chunksToProcess = (bytesRead + chunkSize - 1) / chunkSize; // 计算需要拆分的块数

                    for ( int j = 0; j < chunksToProcess; ++j ) {
                        // 计算当前块的起始和结束位置
                        int start = j * chunkSize;
                        int end = Math.min(start + chunkSize, bytesRead);

                        byte[] chunkData = Arrays.copyOfRange( buffer, start, end ); // 拆分出当前块

                        // 发送当前块的数据
                        fileDistribution.transmitClusterFrame(
                                head,
                                new UFMDClusterFrame(chunkData, path, i, fileClusterNum)
                        );
                    }
                }
            }
            finally {
                fileInputStream.close();
                tempFile.delete();
            }


            ++distributionFrameNum;

            if( distributionFrameNum == 10 ){
                synchronized( this.sessionPhaser.getFileLock( fileNode.getGuid() ) ){
                    this.sessionPhaser.getFileLock( fileNode.getGuid() ).wait();
                }
                distributionFrameNum = 0;
            }
        }


    }

    @Override
    public void test() throws UMBServiceException {
        FileMultiDistributionIface fileDistribution = this.producer.getIface(FileMultiDistributionIface.class,"testTopic");
        FileNode fileNode = this.primaryFileSystem.getFileNode(GUIDs.GUID72("1214792-000373-0003-00"));
        String path = this.primaryFileSystem.getPath(fileNode.getGuid());
        //fileDistribution.startDistribution( path );
        BroadcastControlConsumer consumer = this.getConsumer("testTopic", "testGroup");
        consumer.start();
    }

    @Override
    public BroadcastControlConsumer getConsumer( String topic,String group ) {
        return this.consumer;
    }

    @Override
    public BroadcastControlProducer getProducer() {
        return this.producer;
    }
}
