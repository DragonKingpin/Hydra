package com.walnut.sparta.ucdn.console.umc.ufm;

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
import com.walnut.sparta.ucdn.console.infrastructure.UCDNService;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;
import com.walnut.sparta.ucdn.console.umc.ufm.session.UFMTransaction;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

@Controller
@AddressMapping( "com.pinecone.hydra.uofs.ufm.FileMultiDistributionIface." )
//@Service
public class UCDNFMDController {
    private Logger                            logger;

    protected KOMFileSystem                   primaryFileSystem;


    protected UniformVolumeManager            primaryVolume;


    protected SessionPhaser                   sessionPhaser;


    protected SessionValidator                fileSessionValidator;


    public UCDNFMDController(){

    }

    public UCDNFMDController(MasterWarehouse masterWarehouse, UCDNService ucdnService) throws UMBServiceException {
        this.logger             = LoggerFactory.getLogger( this.getClass() );
        this.primaryFileSystem  = ucdnService.getKOMFileSystem();
        this.primaryVolume      = ucdnService.getUniformVolumeManager();
        this.sessionPhaser      = masterWarehouse.getSessionPhaser();
        this.fileSessionValidator = new UFMSessionValidator(masterWarehouse, ucdnService );
    }

    @AddressMapping("startDistribution")
    public void setFileMate( RequestHead head, String path, long definitionSize ) {
//        if( UCDNConstants.serviceLevel.equals("master") ){
//            return;
//        }
        if( this.sessionPhaser.getSessionTransaction( head.getSessionId() ) != null ){
            logger.info("异常存在的事务");
            this.sessionPhaser.removeSessionTransaction( head.getSessionId() );
            return;
        }

        logger.info( "开始" );
        long sessionId = head.getSessionId();
        FileNode fileNode = this.primaryFileSystem.affirmFileNode( path );
        fileNode.setDefinitionSize( definitionSize );
        this.primaryFileSystem.update( fileNode );
        this.sessionPhaser.registerClusterCount( fileNode.getGuid(),0 );

        UFMTransaction ufmTransaction = new UFMTransaction( fileNode.getGuid() );
        ufmTransaction.setLastEventArrivedMills( System.currentTimeMillis() );
        this.sessionPhaser.registerSessionTransaction( sessionId, ufmTransaction );
        this.sessionPhaser.getSessionTransaction( sessionId ).finishStartTransmit();
    }

    @AddressMapping("setFrameMeta")
    public void setFrameMeta( RequestHead head, UFMDClusterDO frameMeta ) throws IOException {
//        if( UCDNConstants.serviceLevel.equals("master") ){
//            return;
//        }
        long sessionId = head.getSessionId();
        if ( this.assertTransmitTransaction ( frameMeta.getFilePath(), head) ) {
            return;
        }

        logger.info("保存簇信息");
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
    }

    @AddressMapping("transmitClusterFrame")
    public void transmitClusterFrame( RequestHead head, UFMDClusterFrame ufmdClusterFrame ) throws IOException, InterruptedException {
//        if( UCDNConstants.serviceLevel.equals("master") ){
//            return;
//        }
        long sessionId = head.getSessionId();
        if ( this.assertTransmitTransaction ( ufmdClusterFrame.getPath(), head) ) {
            return;
        }

        ElementNode elementNode = this.primaryFileSystem.queryElement(ufmdClusterFrame.getPath());
        Cluster cluster = this.primaryFileSystem.getClusterByFileWithId(elementNode.getGuid(), ufmdClusterFrame.getSegId());

        //logger.info("写入文件内容 簇ID：" + cluster.getSegGuid());
        if( this.sessionPhaser.getClusterLock(cluster.getSegGuid()) == null ){
            this.sessionPhaser.registerClusterLock( cluster.getSegGuid(), new ClusterLock());
        }else {
            synchronized (this.sessionPhaser.getClusterLock(cluster.getSegGuid())){
                this.sessionPhaser.getClusterLock(cluster.getSegGuid()).increment();
                this.sessionPhaser.getClusterLock(cluster.getSegGuid()).wait();
            }
        }

        RandomAccessFile fos = this.sessionPhaser.getClusterOutputStream( cluster.getSegGuid() );

        String path = UCDNConstants.TempFilePath + cluster.getSegGuid() + ".temp";
        File tempFile = new File( path );

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
            if( this.sessionPhaser.getClusterLock(cluster.getSegGuid()).getWaitThreatNum().get() == 0){
                this.sessionPhaser.removeClusterLock( cluster.getSegGuid() );
                return;
            }
            synchronized ( this.sessionPhaser.getClusterLock(cluster.getSegGuid()) ){
                this.sessionPhaser.getClusterLock( cluster.getSegGuid() ).decrement();
                this.sessionPhaser.getClusterLock(cluster.getSegGuid()).notify();
            }
        }

    }

    //todo 添加写完后向主节点发送完成指令
    @AddressMapping("frameTerminate")
    public void frameTerminate( RequestHead head, String path, long segId, long totalSegNum ) throws IOException {
//        if( UCDNConstants.serviceLevel.equals("master") ){
//            return;
//        }
        long sessionId = head.getSessionId();
        if ( this.assertTransmitTransaction ( path, head) ) {
            return;
        }

        logger.info("结束");
        FileNode fileNode = (FileNode) this.primaryFileSystem.queryElement(path);
        LocalCluster frame = (LocalCluster)this.primaryFileSystem.getClusterByFileWithId(fileNode.getGuid(), segId);
        File tempFile = new File(UCDNConstants.TempFilePath + frame.getSegGuid() + ".temp");

        try{
            tempFile.createNewFile();

            FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.READ);
            TitanFileChannelChanface chanface = new TitanFileChannelChanface( channel );

            TitanFileReceiveEntity64 receiveEntity64 = new TitanFileReceiveEntity64(this.primaryFileSystem, path, fileNode, chanface, this.primaryVolume);
            receiveEntity64.receive( segId );



            this.sessionPhaser.incrementClusterCount( fileNode.getGuid() );
            logger.info("目前已完成簇数量：" + this.sessionPhaser.getClusterCount( fileNode.getGuid() ));
            if( this.sessionPhaser.getClusterCount( fileNode.getGuid() ) == 10 ){
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
    }

    protected boolean assertTransmitTransaction( String filePath, RequestHead head ) throws IOException {
        long sessionId = head.getSessionId();
        UFMTransaction transaction = this.sessionPhaser.getSessionTransaction(sessionId);
        if( transaction == null ){
            logger.info( "不存在的事务，直接忽略" );
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if( currentTimeMillis - transaction.getLastEventArrivedMills() > UCDNConstants.expireTimeMillis ){
            logger.info( "事务过期" );
            this.sessionPhaser.removeSessionTransaction( sessionId );
            this.transmitRollBack( filePath, sessionId );
            return true;
        }
        if( !transaction.isStartTransmit() ){
            logger.info( "异常的事务流程" );
            this.sessionPhaser.removeSessionTransaction( sessionId );
            this.transmitRollBack( filePath, sessionId );
            return true;
        }
        return false;
    }

    private void transmitRollBack( String filePath, long sessionId ) throws IOException {
        logger.info("事务异常开始回滚");
        FileNode fileNode = (FileNode) this.primaryFileSystem.queryElement(filePath);

        ClusterPage clusterPage = this.primaryFileSystem.fetchClustersByFileGuid( fileNode.getGuid() );

        long fileClusterNum = clusterPage.getClusters();

        for( long i = 0; i < fileClusterNum; i++ ){
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
    }

}
