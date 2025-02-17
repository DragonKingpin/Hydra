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
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;
import com.walnut.sparta.ucdn.console.umc.ufm.session.UFMTransaction;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

@Slf4j
@Controller
@AddressMapping( "com.pinecone.hydra.uofs.ufm.FileMultiDistributionIface." )
//@Service
public class UCDNFMDController {

//    @Resource
    protected KOMFileSystem                   primaryFileSystem;

//    @Resource
    protected UniformVolumeManager            primaryVolume;

//    @Resource
    protected SessionPhaser                   sessionPhaser;

//    @Resource
    protected SessionValidator                fileSessionValidator;


    public UCDNFMDController(){

    }

    public UCDNFMDController(MasterWarehouse masterWarehouse ) throws UMBServiceException {
        this.primaryFileSystem  = masterWarehouse.getKOMFileSystem();
        this.primaryVolume      = masterWarehouse.getUniformVolumeManager();
        this.sessionPhaser      = masterWarehouse.getSessionPhaser();
        this.fileSessionValidator = new UFMSessionValidator( masterWarehouse );
    }

    @AddressMapping("startDistribution")
    public void setFileMate( RequestHead head, String path, long definitionSize ) {
        if( this.sessionPhaser.getSessionTransaction( head.getSessionId() ) != null ){
            log.info("异常存在的事务");
            this.sessionPhaser.removeSessionTransaction( head.getSessionId() );
            return;
        }

        log.info( "开始" );
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
        long sessionId = head.getSessionId();
        if ( this.assertTransmitTransaction ( frameMeta.getFilePath(), head) ) {
            return;
        }

        log.info("保存簇信息");
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
        long sessionId = head.getSessionId();
        if ( this.assertTransmitTransaction ( ufmdClusterFrame.getPath(), head) ) {
            return;
        }

        ElementNode elementNode = this.primaryFileSystem.queryElement(ufmdClusterFrame.getPath());
        Cluster cluster = this.primaryFileSystem.getClusterByFileWithId(elementNode.getGuid(), ufmdClusterFrame.getSegId());

        //log.info("写入文件内容 簇ID：" + cluster.getSegGuid());
        if( this.sessionPhaser.getClusterLock(cluster.getSegGuid()) == null ){
            this.sessionPhaser.registerClusterLock( cluster.getSegGuid(), new ClusterLock());
        }else {
            synchronized (this.sessionPhaser.getClusterLock(cluster.getSegGuid())){
                this.sessionPhaser.getClusterLock(cluster.getSegGuid()).increment();
                this.sessionPhaser.getClusterLock(cluster.getSegGuid()).wait();
            }
        }

        FileOutputStream fos = this.sessionPhaser.getClusterOutputStream( cluster.getSegGuid() );

        String path = UCDNConstants.TempFilePath + cluster.getSegGuid() + ".temp";
        File tempFile = new File( path );

        if( fos == null ){
            fos =  new FileOutputStream( tempFile,true );
            this.sessionPhaser.registerClusterOutputStream( cluster.getSegGuid(), fos );
        }

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
        long sessionId = head.getSessionId();
        if ( this.assertTransmitTransaction ( path, head) ) {
            return;
        }

        log.info("结束");
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
            log.info("目前已完成簇数量：" + this.sessionPhaser.getClusterCount( fileNode.getGuid() ));
            if( this.sessionPhaser.getClusterCount( fileNode.getGuid() ) == 10 ){
                this.sessionPhaser.resetClusterCount( fileNode.getGuid() );
                this.fileSessionValidator.stageClusterGroupComplete( path );
            }
        }
        finally {
            FileOutputStream outputStream = this.sessionPhaser.getClusterOutputStream(frame.getSegGuid());
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
            log.info( "不存在的事务，直接忽略" );
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if( currentTimeMillis - transaction.getLastEventArrivedMills() > UCDNConstants.expireTimeMillis ){
            log.info( "事务过期" );
            this.sessionPhaser.removeSessionTransaction( sessionId );
            this.transmitRollBack( filePath, sessionId );
            return true;
        }
        if( !transaction.isStartTransmit() ){
            log.info( "异常的事务流程" );
            this.sessionPhaser.removeSessionTransaction( sessionId );
            this.transmitRollBack( filePath, sessionId );
            return true;
        }
        return false;
    }

    private void transmitRollBack( String filePath, long sessionId ) throws IOException {
        log.info("事务异常开始回滚");
        FileNode fileNode = (FileNode) this.primaryFileSystem.queryElement(filePath);

        ClusterPage clusterPage = this.primaryFileSystem.fetchClustersByFileGuid( fileNode.getGuid() );

        long fileClusterNum = clusterPage.getClusters();

        for( long i = 0; i < fileClusterNum; i++ ){
            LocalCluster frame = clusterPage.getLocalCluster( i );
            FileOutputStream clusterOutputStream = this.sessionPhaser.getClusterOutputStream(frame.getSegGuid());
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
