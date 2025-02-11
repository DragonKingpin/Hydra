package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.Frame;
import com.pinecone.hydra.storage.file.entity.LocalFrame;
import com.pinecone.hydra.storage.file.entity.RemoteFrame;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.volume.UnifiedTransmitConstructor;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.infrastructure.entity.UFMDClusterFrame;
import com.walnut.sparta.ucdn.console.infrastructure.entity.UFMDClusterDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

@Controller
@AddressMapping( "com.walnut.sparta.ucdn.console.umc.FileDistribution." )
@Service
public class FileDistributionController {

    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private UniformVolumeManager primaryVolume;

    private UnifiedTransmitConstructor constructor;

    public FileDistributionController(){
        this.constructor = new UnifiedTransmitConstructor();
    }

    @AddressMapping("setFileMeta")
    public void setFileMate( String path,long definitionSize ){
        Debug.trace("保存文件信息");
        FileNode fileNode = this.primaryFileSystem.affirmFileNode( path );
        fileNode.setDefinitionSize( definitionSize );
        this.primaryFileSystem.update( fileNode );
    }

    @AddressMapping("setFrameMeta")
    public void setFrameMeta(UFMDClusterDO frameMeta){
        Debug.trace("保存簇信息");
        FSNodeAllotment allotment = this.primaryFileSystem.getFSNodeAllotment();
        String filePath = frameMeta.getFilePath();
        ElementNode elementNode = this.primaryFileSystem.queryElement(filePath);
        LocalFrame localFrame = allotment.newLocalFrame();
        RemoteFrame remoteFrame = allotment.newRemoteFrame( elementNode.getGuid(),(int)frameMeta.getSegId() );
        remoteFrame.setDeviceGuid(this.primaryFileSystem.getConfig().getLocalhostGUID());
        remoteFrame.setSegGuid( localFrame.getSegGuid() );

        localFrame.setSegId(frameMeta.getSegId() );
        localFrame.setSourceName( frameMeta.getSourceName() );
        localFrame.setSize(frameMeta.getSize() );
        localFrame.setFileGuid( elementNode.getGuid() );

        localFrame.save();
        remoteFrame.save();
    }

    @AddressMapping("saveFrameContent")
    public void saveFrameContent(UFMDClusterFrame ufmdClusterFrame) throws IOException {
        Debug.trace("写入文件内容");
        ElementNode elementNode = this.primaryFileSystem.queryElement(ufmdClusterFrame.getPath());
        Frame frame = this.primaryFileSystem.getFrameByFileWithId(elementNode.getGuid(), ufmdClusterFrame.getSegId());
        File tempFile = File.createTempFile( frame.getSegGuid().toString(),".temp" );
        Debug.trace(tempFile.getPath());
        try (FileOutputStream fos = new FileOutputStream(tempFile,true)) {
            fos.write(ufmdClusterFrame.getBytes());
        } catch (IOException e) {
            throw e;
        }
    }

    //todo 添加写完后向主节点发送完成指令
    @AddressMapping("frameEnd")
    public void frameEnd(String path, long segId) throws IOException {
        Debug.trace("结束");
        FileNode fileNode = (FileNode) this.primaryFileSystem.queryElement(path);
        LocalFrame frame = (LocalFrame)this.primaryFileSystem.getFrameByFileWithId(fileNode.getGuid(), segId);
        File tempFile = File.createTempFile( "temp",frame.getSegGuid().toJSONString() );
        FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.READ);
        TitanFileChannelChanface chanface = new TitanFileChannelChanface( channel );

        TitanFileReceiveEntity64 receiveEntity64 = new TitanFileReceiveEntity64(this.primaryFileSystem, path, fileNode, chanface, this.primaryVolume);
        receiveEntity64.receive( segId );
        tempFile.delete();
    }

}
