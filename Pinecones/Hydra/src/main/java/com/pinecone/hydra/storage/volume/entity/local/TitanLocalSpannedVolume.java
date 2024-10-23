package com.pinecone.hydra.storage.volume.entity.local;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.ArchLogicVolume;
import com.pinecone.hydra.storage.volume.source.SpannedVolumeManipulator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class TitanLocalSpannedVolume extends ArchLogicVolume implements LocalSpannedVolume {
    private SpannedVolumeManipulator spannedVolumeManipulator;

    public TitanLocalSpannedVolume(VolumeTree volumeTree, SpannedVolumeManipulator spannedVolumeManipulator) {
        super(volumeTree);
        this.spannedVolumeManipulator = spannedVolumeManipulator;
    }
    public void setSpannedVolumeManipulator( SpannedVolumeManipulator spannedVolumeManipulator ){
        this.spannedVolumeManipulator = spannedVolumeManipulator;
    }
    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public void channelExport(KOMFileSystem fileSystem, FileNode file) throws IOException {

    }

    @Override
    public void streamExport(KOMFileSystem fileSystem, FileNode file) throws IOException {

    }

    @Override
    public void channelReceive(KOMFileSystem fileSystem, FileNode file, FileChannel channel) throws IOException {

    }

    @Override
    public void extendLogicalVolume(GUID physicalGuid) {

    }

    @Override
    public List<GUID> lsblk() {
        return null;
    }

    @Override
    public void streamReceive(KOMFileSystem fileSystem, FileNode file, InputStream inputStream) {

    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
