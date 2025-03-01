package com.walnut.sparta.ucdn.console.ufm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ClusterPage;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.Cluster;
import com.pinecone.hydra.storage.file.entity.LocalCluster;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.infrastructure.ClusterLock;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.ufm.protocol.RequestHead;
import com.walnut.sparta.ucdn.console.ufm.session.UFMTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Controller
@AddressMapping( "com.pinecone.hydra.uofs.ufm.FileMultiDistributionIface." )
public class FileMultiDistributionController implements Pinenut {
    private Logger                            logger;

    protected KOMFileSystem                   primaryFileSystem;

    protected UniformVolumeManager            primaryVolume;

    protected SessionPhaser                   sessionPhaser;

    protected SessionValidator                fileSessionValidator;

    protected UFMConfig                       config;

    public FileMultiDistributionController( UOFSFileMultiDistributionService distributionService ) throws UMBServiceException {
        this.logger                 = LoggerFactory.getLogger( this.getClass() );
        this.primaryFileSystem      = distributionService.primaryFileSystem;
        this.primaryVolume          = distributionService.primaryVolume;
        this.sessionPhaser          = distributionService.sessionPhaser;
        this.fileSessionValidator   = distributionService.fileSessionValidator;
        this.config                 = distributionService.config;
    }

    @AddressMapping("startDistribution")
    public void setFileMate( RequestHead head, String path, long definitionSize ) {
        if( this.sessionPhaser.getSessionTransaction( head.getSessionId() ) != null ){
            this.logger.warn( "[Warning] UCDNService `startDistribution` session assertion compromised." );
            this.sessionPhaser.removeSessionTransaction( head.getSessionId() );
            return;
        }

        this.logger.info( "UCDNService invoked `startDistribution`. <Start>" );

        long sessionId = head.getSessionId();
        FileNode fileNode = this.primaryFileSystem.affirmFileNode( path );
        fileNode.setDefinitionSize( definitionSize );
        this.primaryFileSystem.update( fileNode );
        this.sessionPhaser.registerClusterCount( fileNode.getGuid(),0 );

        UFMTransaction ufmTransaction = new UFMTransaction( fileNode.getGuid() );
        ufmTransaction.setLastEventArrivedMills( System.currentTimeMillis() );
        this.sessionPhaser.registerSessionTransaction( sessionId, ufmTransaction );
        this.sessionPhaser.getSessionTransaction( sessionId ).finishStartTransmit();

        this.logger.info( "UCDNService invoked `startDistribution`. <Done>" );
    }

    @AddressMapping("setFrameMeta")
    public void setFrameMeta( RequestHead head, UFMDClusterDO frameMeta ) throws IOException {
        long sessionId = head.getSessionId();
        if ( this.assertTransmitTransaction ( frameMeta.getFilePath(), head) ) {
            this.logger.warn( "[Warning] UCDNService `setFrameMeta` session assertion compromised." );
            return;
        }

        this.logger.info( "UCDNService invoked `setFrameMeta`. <Start>" );
        FSNodeAllotment allotment = this.primaryFileSystem.getFSNodeAllotment();
        String filePath = frameMeta.getFilePath();
        ElementNode elementNode = this.primaryFileSystem.queryElement(filePath);
        LocalCluster localCluster = allotment.newLocalCluster();

        localCluster.setSegId(frameMeta.getSegId() );
        localCluster.setSourceName( frameMeta.getSourceName() );
        localCluster.setSize(frameMeta.getSize() );
        localCluster.setFileGuid( elementNode.getGuid() );

        localCluster.save();
        this.sessionPhaser.getSessionTransaction( sessionId ).setLastEventArrivedMills( System.currentTimeMillis() );

        this.logger.info( "UCDNService invoked `setFrameMeta`. <Done>" );
    }

    @AddressMapping("transmitClusterFrame")
    public void transmitClusterFrame( RequestHead head, UFMDClusterFrame ufmdClusterFrame ) throws IOException, InterruptedException {
        long sessionId = head.getSessionId();
        if ( this.assertTransmitTransaction ( ufmdClusterFrame.getPath(), head) ) {
            this.logger.warn( "[Warning] UCDNService `transmitClusterFrame` session assertion compromised." );
            return;
        }

        this.logger.info( "UCDNService invoked `transmitClusterFrame`. <Start>" );
        ElementNode elementNode = this.primaryFileSystem.queryElement(ufmdClusterFrame.getPath());
        Cluster cluster = this.primaryFileSystem.getClusterByFileWithId(elementNode.getGuid(), ufmdClusterFrame.getSegId());

        if( this.sessionPhaser.getClusterLock(cluster.getSegGuid()) == null ){
            this.sessionPhaser.registerClusterLock( cluster.getSegGuid(), new ClusterLock());
        }
        else {
            synchronized (this.sessionPhaser.getClusterLock(cluster.getSegGuid())){
                this.sessionPhaser.getClusterLock(cluster.getSegGuid()).increment();
                this.sessionPhaser.getClusterLock(cluster.getSegGuid()).wait();
            }
        }

        RandomAccessFile fos = this.sessionPhaser.getClusterOutputStream( cluster.getSegGuid() );

        Path     temporaryPath = this.config.formatTemporaryPath( cluster.getSegGuid().toString() );
        String szTemporaryPath = temporaryPath.toString();
        File tempFile = new File( szTemporaryPath );
        if( fos == null ){
            fos =  new RandomAccessFile( tempFile,"rw" );
            this.sessionPhaser.registerClusterOutputStream( cluster.getSegGuid(), fos );
        }

        fos.seek(ufmdClusterFrame.getOffset() );
        fos.write( ufmdClusterFrame.getBytes() );

        this.sessionPhaser.getSessionTransaction( sessionId ).setLastEventArrivedMills( System.currentTimeMillis() );

        if( cluster.getSize() == tempFile.length() ) {
            this.sessionPhaser.removeClusterCount( cluster.getSegGuid() );
            this.frameTerminate( head, ufmdClusterFrame.getPath(), ufmdClusterFrame.getSegId(), ufmdClusterFrame.getTotalSegNum() );
        }

        if( this.sessionPhaser.getClusterLock(cluster.getSegGuid()) != null ){
            if( this.sessionPhaser.getClusterLock(cluster.getSegGuid()).getWaitThreatNum().get() == 0 ){
                this.sessionPhaser.removeClusterLock( cluster.getSegGuid() );
                this.logger.info( "UCDNService invoked `transmitClusterFrame`. <Done>" );
                return;
            }
            synchronized ( this.sessionPhaser.getClusterLock(cluster.getSegGuid()) ){
                this.sessionPhaser.getClusterLock( cluster.getSegGuid() ).decrement();
                this.sessionPhaser.getClusterLock(cluster.getSegGuid()).notify();
            }
        }

        this.logger.info( "UCDNService invoked `transmitClusterFrame`. <Done>" );
    }

    //todo 添加写完后向主节点发送完成指令
    @AddressMapping("frameTerminate")
    public void frameTerminate( RequestHead head, String path, long segId, long totalSegNum ) throws IOException {
        long sessionId = head.getSessionId();
        if ( this.assertTransmitTransaction ( path, head) ) {
            this.logger.warn( "[Warning] UCDNService `frameTerminate` session assertion compromised." );
            return;
        }

        this.logger.info( "UCDNService invoked `frameTerminate`. <Start>" );
        FileNode fileNode = (FileNode) this.primaryFileSystem.queryElement(path);
        LocalCluster frame = (LocalCluster)this.primaryFileSystem.getClusterByFileWithId(fileNode.getGuid(), segId);

        Path     temporaryPath = this.config.formatTemporaryPath( frame.getSegGuid().toString() );
        String szTemporaryPath = temporaryPath.toString();
        File          tempFile = new File( szTemporaryPath );
        try {
            if ( !tempFile.exists() ){
                throw new IOException( "Creating file compromised, what :" + szTemporaryPath );
            }

            FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.READ);
            TitanFileChannelChanface chanface = new TitanFileChannelChanface( channel );

            TitanFileReceiveEntity64 receiveEntity64 = new TitanFileReceiveEntity64(this.primaryFileSystem, path, fileNode, chanface, this.primaryVolume);
            receiveEntity64.receive( segId );

            this.sessionPhaser.incrementClusterCount( fileNode.getGuid() );
            this.logger.info("`frameTerminate` Currently finished transition cluster：" + this.sessionPhaser.getClusterCount( fileNode.getGuid() ));
            if( this.sessionPhaser.getClusterCount( fileNode.getGuid() ) == this.config.getBatchTransmitMemberThreshold() ){
                this.sessionPhaser.resetClusterCount( fileNode.getGuid() );
                this.fileSessionValidator.stageClusterGroupComplete( path );
            }
        }
        finally {
            RandomAccessFile outputStream = this.sessionPhaser.getClusterOutputStream(frame.getSegGuid());
            outputStream.close();
            this.sessionPhaser.removeClusterOutputStream( frame.getSegGuid() );
            if ( !tempFile.delete() ) {
                throw new IOException( "Temporary file has been purged failed." );
            }
            if( segId == totalSegNum - 1 ){
                this.sessionPhaser.getSessionTransaction( sessionId ).setLastEventArrivedMills( System.currentTimeMillis() );
                this.sessionPhaser.getSessionTransaction( sessionId ).finishTransmitFileContent();
                this.sessionPhaser.getSessionTransaction( sessionId ).finishFileDistributionComplete();
                this.sessionPhaser.removeClusterCount( fileNode.getGuid() );
                this.sessionPhaser.removeFileLock( fileNode.getGuid() );
                this.sessionPhaser.removeConsumerCount( fileNode.getGuid() );
                this.sessionPhaser.removeSessionTransaction( sessionId );
                this.fileSessionValidator.fileTransmitComplete( path, UCDNConstants.serviceId );
            }
            else {
                this.sessionPhaser.getSessionTransaction( sessionId ).setLastEventArrivedMills( System.currentTimeMillis() );
            }
        }

        this.logger.info( "UCDNService invoked `frameTerminate`. <Done>" );
    }

    protected boolean assertTransmitTransaction( String filePath, RequestHead head ) throws IOException {
        long sessionId = head.getSessionId();
        UFMTransaction transaction = this.sessionPhaser.getSessionTransaction(sessionId);
        if( transaction == null ){
            this.logger.warn( "[Warning] UCDNService `assertTransmitTransaction` session doesn`t existed. <Pass>" );
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if( currentTimeMillis - transaction.getLastEventArrivedMills() > UCDNConstants.expireTimeMillis ){
            this.logger.warn( "[Warning] UCDNService `assertTransmitTransaction` session has expired. <Pass>" );
            this.sessionPhaser.removeSessionTransaction( sessionId );
            this.transmitRollBack( filePath, sessionId );
            return true;
        }
        if( !transaction.isStartTransmit() ){
            this.logger.warn( "[Warning] UCDNService `assertTransmitTransaction` illegal transaction stage, which should never has started yet. <Pass>" );
            this.sessionPhaser.removeSessionTransaction( sessionId );
            this.transmitRollBack( filePath, sessionId );
            return true;
        }
        return false;
    }

    private void transmitRollBack( String filePath, long sessionId ) throws IOException {
        this.logger.warn( "[Warning] UCDNService `transmitRollBack`. <Start>" );

        FileNode fileNode = (FileNode) this.primaryFileSystem.queryElement(filePath);
        ClusterPage clusterPage = this.primaryFileSystem.fetchClustersByFileGuid( fileNode.getGuid() );

        long fileClusterNum = clusterPage.getClusters();

        for( long i = 0; i < fileClusterNum; ++i ){
            LocalCluster frame = clusterPage.getLocalCluster( i );
            RandomAccessFile clusterOutputStream = this.sessionPhaser.getClusterOutputStream(frame.getSegGuid());
            clusterOutputStream.close();
            this.sessionPhaser.removeClusterOutputStream( frame.getSegGuid() );
        }
        this.sessionPhaser.removeClusterCount( fileNode.getGuid() );
        this.sessionPhaser.removeFileLock( fileNode.getGuid() );
        this.sessionPhaser.removeClusterCount( fileNode.getGuid() );
        //this.primaryFileSystem.remove( fileNode.getGuid() );
        this.sessionPhaser.removeSessionTransaction( sessionId );

        this.logger.warn( "[Warning] UCDNService `transmitRollBack`. <Done>" );
    }

}
