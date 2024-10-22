package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public interface LogicVolume extends Volume, TreeNode {
    String getName();

    void setName( String name );

    List<Volume> getChildren();

    void setChildren( List<Volume> children );

    VolumeCapacity getVolumeCapacity();

    void setVolumeCapacity( VolumeCapacity volumeCapacity );

    MountPoint getMountPoint();
    void setMountPoint( MountPoint mountPoint );

    void channelExport( KOMFileSystem fileSystem, FileNode file ) throws IOException;
    void streamExport( KOMFileSystem fileSystem, FileNode file ) throws IOException;
    void channelReceiver(KOMFileSystem fileSystem, FileNode file, FileChannel channel) throws IOException;
    void streamReceiver(KOMFileSystem fileSystem, FileNode file, InputStream inputStream) throws IOException;

    default MirroredVolume evinceMirroredVolume(){
        return null;
    }
    default SimpleVolume   evinceSimpleVolume(){
        return null;
    }
    default SpannedVolume  evinceSpannedVolume(){
        return null;
    }
    default StripedVolume  evinceStripeVolume(){
        return null;
    }
}
