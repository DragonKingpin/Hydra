package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.Frame;
import com.pinecone.hydra.storage.file.entity.LocalFrame;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.domain.ufm.SessionPhaser;
import com.walnut.sparta.ucdn.console.domain.ufm.FileDistributionSynchronize;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.domain.ufm.UFMDClusterFrame;
import com.walnut.sparta.ucdn.console.domain.ufm.UFMDClusterDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

@Controller
@AddressMapping( "com.walnut.sparta.ucdn.console.umc.FileDistribution." )
@Service
public class FileDistributionController {

    @Resource
    private KOMFileSystem                   primaryFileSystem;

    @Resource
    private UniformVolumeManager            primaryVolume;

    @Resource
    SessionPhaser                           sessionPhaser;

    @Resource
    FileDistributionSynchronize             fileDistributionSynchronize;

    @Resource
    Transmit                                transmit;

    public FileDistributionController(){

    }

    @AddressMapping("setFileMeta")
    public void setFileMate( String path,long definitionSize ){
        Debug.trace("保存文件信息");
        FileNode fileNode = this.primaryFileSystem.affirmFileNode( path );
        fileNode.setDefinitionSize( definitionSize );
        this.primaryFileSystem.update( fileNode );
        this.sessionPhaser.registerClusterCount( fileNode.getGuid(),0 );
    }

    @AddressMapping("setFrameMeta")
    public void setFrameMeta(UFMDClusterDO frameMeta){
        Debug.trace("保存簇信息");
        FSNodeAllotment allotment = this.primaryFileSystem.getFSNodeAllotment();
        String filePath = frameMeta.getFilePath();
        ElementNode elementNode = this.primaryFileSystem.queryElement(filePath);
        LocalFrame localFrame = allotment.newLocalFrame();

        localFrame.setSegId(frameMeta.getSegId() );
        localFrame.setSourceName( frameMeta.getSourceName() );
        localFrame.setSize(frameMeta.getSize() );
        localFrame.setFileGuid( elementNode.getGuid() );

        localFrame.save();
    }

    @AddressMapping("saveFrameContent")
    public void saveFrameContent(UFMDClusterFrame ufmdClusterFrame) throws IOException {
        Debug.trace("写入文件内容");
        ElementNode elementNode = this.primaryFileSystem.queryElement(ufmdClusterFrame.getPath());
        Frame frame = this.primaryFileSystem.getFrameByFileWithId(elementNode.getGuid(), ufmdClusterFrame.getSegId());
        String path = UCDNConstants.TempFilePath + frame.getSegGuid() + ".temp";

        File file = transmit.bytesToFile(path, ufmdClusterFrame.getBytes());
        if( frame.getSize() == file.length() ){
            this.frameEnd( ufmdClusterFrame.getPath(), ufmdClusterFrame.getSegId() );
        }
    }

    //todo 添加写完后向主节点发送完成指令
    @AddressMapping("frameEnd")
    public void frameEnd(String path, long segId) throws IOException {
        Debug.trace("结束");
        FileNode fileNode = (FileNode) this.primaryFileSystem.queryElement(path);
        LocalFrame frame = (LocalFrame)this.primaryFileSystem.getFrameByFileWithId(fileNode.getGuid(), segId);
        File tempFile = new File(UCDNConstants.TempFilePath + frame.getSegGuid() + ".temp");
        tempFile.createNewFile();

        FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.READ);
        TitanFileChannelChanface chanface = new TitanFileChannelChanface( channel );

        TitanFileReceiveEntity64 receiveEntity64 = new TitanFileReceiveEntity64(this.primaryFileSystem, path, fileNode, chanface, this.primaryVolume);
        receiveEntity64.receive( segId );
        tempFile.delete();
        this.sessionPhaser.incrementClusterCount( fileNode.getGuid() );
        Debug.trace("目前已完成簇数量：" + this.sessionPhaser.getClusterCount( fileNode.getGuid() ));
        if( this.sessionPhaser.getClusterCount( fileNode.getGuid() ) == 10 ){
            this.sessionPhaser.resetClusterCount( fileNode.getGuid() );
            this.fileDistributionSynchronize.distributionCallBack( path );
        }
    }

}
