package com.pinecone.hydra.storage.file.transmit.exporter;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.transmit.exporter.channel.ChannelExporterEntity;
import com.pinecone.hydra.storage.file.transmit.exporter.stream.StreamExporterEntity;

public interface ExporterEntity extends Pinenut {
    KOMFileSystem getFileSystem();
    void setFileSystem( KOMFileSystem fileSystem );
    FileNode getFile();
    void setFile( FileNode file );
    default ChannelExporterEntity evinceChannelExporterEntity(){
        return null;
    }
    default StreamExporterEntity evinceStreamExporterEntity(){
        return null;
    }
}