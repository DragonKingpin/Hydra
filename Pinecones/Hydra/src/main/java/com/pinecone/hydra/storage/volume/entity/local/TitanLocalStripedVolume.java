package com.pinecone.hydra.storage.volume.entity.local;

import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.ArchLogicVolume;
import com.pinecone.hydra.storage.volume.source.StripedVolumeManipulator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public class TitanLocalStripedVolume extends ArchLogicVolume implements LocalStripedVolume {
    private StripedVolumeManipulator stripedVolumeManipulator;

    public TitanLocalStripedVolume(VolumeTree volumeTree, StripedVolumeManipulator stripedVolumeManipulator) {
        super(volumeTree);
        this.stripedVolumeManipulator = stripedVolumeManipulator;
    }

    @Override
    public void channelExport(KOMFileSystem fileSystem, FileNode file) throws IOException {

    }

    @Override
    public void streamExport(KOMFileSystem fileSystem, FileNode file) throws IOException {

    }

    @Override
    public void channelReceiver(KOMFileSystem fileSystem, FileNode file, FileChannel channel) throws IOException {

    }

    @Override
    public void streamReceiver(KOMFileSystem fileSystem, FileNode file, InputStream inputStream) {

    }

    public void setStripedVolumeManipulator(StripedVolumeManipulator stripedVolumeManipulator ){
        this.stripedVolumeManipulator = stripedVolumeManipulator;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
