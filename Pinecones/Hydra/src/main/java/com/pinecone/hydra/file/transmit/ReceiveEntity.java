package com.pinecone.hydra.file.transmit;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.entity.FileNode;

public interface ReceiveEntity extends Pinenut {
    KOMFileSystem getFileSystem();
    void setFileSystem( KOMFileSystem fileSystem );

    String getDestDirPath();
    void setDestDirPath( String destDirPath );

    FileNode getFile();
    void setFile( FileNode file );

    default ChannelReceiverEntity evinceChannelReceiverEntity(){
        return null;
    }
}
