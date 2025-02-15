package com.walnut.sparta.ucdn.console.umc.ufm;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.file.KOMFileSystem;
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
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

@Controller
@AddressMapping( "com.pinecone.hydra.uofs.ufm.FileMultiDistributionIface." )
//@Service
public class UCdnFMDController {

//    @Resource
    private KOMFileSystem                   primaryFileSystem;

//    @Resource
    private UniformVolumeManager            primaryVolume;

//    @Resource
    SessionPhaser                           sessionPhaser;

//    @Resource
    SessionValidator fileSessionValidator;


    public UCdnFMDController(){

    }

    public UCdnFMDController(MasterWarehouse masterWarehouse ) throws UMBServiceException {
        this.primaryFileSystem  = masterWarehouse.getKOMFileSystem();
        this.primaryVolume      = masterWarehouse.getUniformVolumeManager();
        this.sessionPhaser      = masterWarehouse.getSessionPhaser();
        this.fileSessionValidator = new UFMSessionValidator( masterWarehouse );
    }

    @AddressMapping("startDistribution")
    public void setFileMate( RequestHead head, String path, long definitionSize ) {
        Debug.trace("保存文件信息");
        FileNode fileNode = this.primaryFileSystem.affirmFileNode( path + ".bak" );
        fileNode.setDefinitionSize( definitionSize );
        this.primaryFileSystem.update( fileNode );
        this.sessionPhaser.registerClusterCount( fileNode.getGuid(),0 );
    }

    @AddressMapping("setFrameMeta")
    public void setFrameMeta( RequestHead head, UFMDClusterDO frameMeta ) {
        Debug.trace("保存簇信息");
        FSNodeAllotment allotment = this.primaryFileSystem.getFSNodeAllotment();
        String filePath = frameMeta.getFilePath();
        ElementNode elementNode = this.primaryFileSystem.queryElement(filePath);
        LocalCluster localCluster = allotment.newLocalCluster();

        localCluster.setSegId(frameMeta.getSegId() );
        localCluster.setSourceName( frameMeta.getSourceName() );
        localCluster.setSize(frameMeta.getSize() );
        localCluster.setFileGuid( elementNode.getGuid() );

        localCluster.save();
    }

    @AddressMapping("transmitClusterFrame")
    public void transmitClusterFrame( RequestHead head, UFMDClusterFrame ufmdClusterFrame ) throws IOException {
        long currentEventMills = System.currentTimeMillis();

        Debug.trace("写入文件内容");
        ElementNode elementNode = this.primaryFileSystem.queryElement(ufmdClusterFrame.getPath());
        Cluster cluster = this.primaryFileSystem.getClusterByFileWithId(elementNode.getGuid(), ufmdClusterFrame.getSegId());
        String path = UCDNConstants.TempFilePath + cluster.getSegGuid() + ".temp";


        File tempFile = new File( path );
        try ( FileOutputStream fos = new FileOutputStream( tempFile,true ) ) {
            fos.write( ufmdClusterFrame.getBytes() );
        }


        if( cluster.getSize() == tempFile.length() ) {
            RequestHead requestHead = new RequestHead();
            this.frameTerminate( requestHead, ufmdClusterFrame.getPath(), ufmdClusterFrame.getSegId() );
        }

    }

    //todo 添加写完后向主节点发送完成指令
    @AddressMapping("frameTerminate")
    public void frameTerminate( RequestHead head, String path, long segId ) throws IOException {
        Debug.trace("结束");
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
            Debug.trace("目前已完成簇数量：" + this.sessionPhaser.getClusterCount( fileNode.getGuid() ));
            if( this.sessionPhaser.getClusterCount( fileNode.getGuid() ) == 10 ){
                this.sessionPhaser.resetClusterCount( fileNode.getGuid() );
                this.fileSessionValidator.stageClusterGroupComplete( path );
            }
        }
        finally {
            if ( !tempFile.delete() ) {
                throw new IOException( "Temporary file has been purged failed." );
            }
        }
    }

}
