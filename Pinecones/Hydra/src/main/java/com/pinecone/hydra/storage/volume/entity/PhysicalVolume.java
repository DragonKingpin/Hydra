package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public interface PhysicalVolume extends Volume{
    MountPoint getMountPoint();
    void setMountPoint( MountPoint mountPoint );


    void channelExport(KOMFileSystem fileSystem, FileNode file ) throws IOException;
    void streamExport( KOMFileSystem fileSystem, FileNode file ) throws IOException;
    void channelReceive(KOMFileSystem fileSystem, FileNode file, FileChannel channel) throws IOException;
    void channelReceive( KOMFileSystem fileSystem, FileNode file, FileChannel channel, long offset, long endSize ) throws IOException;
    void streamReceive(KOMFileSystem fileSystem, FileNode file, InputStream inputStream) throws IOException;
}
