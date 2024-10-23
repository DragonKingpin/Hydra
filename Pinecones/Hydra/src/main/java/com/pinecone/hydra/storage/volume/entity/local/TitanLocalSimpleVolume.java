package com.pinecone.hydra.storage.volume.entity.local;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.transmit.exporter.channel.GenericChannelExporterEntity;
import com.pinecone.hydra.storage.file.transmit.exporter.stream.GenericStreamExporterEntity;
import com.pinecone.hydra.storage.file.transmit.receiver.channel.GenericChannelReceiveEntity;
import com.pinecone.hydra.storage.file.transmit.receiver.channel.GenericChannelReceiver;
import com.pinecone.hydra.storage.file.transmit.receiver.stream.GenericStreamReceiverEntity;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.ArchLogicVolume;
import com.pinecone.hydra.storage.volume.entity.PhysicalVolume;
import com.pinecone.hydra.storage.volume.entity.Volume;
import com.pinecone.hydra.storage.volume.source.SimpleVolumeManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class TitanLocalSimpleVolume extends ArchLogicVolume implements LocalSimpleVolume {
    private SimpleVolumeManipulator simpleVolumeManipulator;


    public TitanLocalSimpleVolume(VolumeTree volumeTree, SimpleVolumeManipulator simpleVolumeManipulator) {
        super(volumeTree);
        this.simpleVolumeManipulator = simpleVolumeManipulator;
    }

    public TitanLocalSimpleVolume( VolumeTree volumeTree ){
        super( volumeTree );
    }

    public TitanLocalSimpleVolume(){
    }

    public void setSimpleVolumeManipulator( SimpleVolumeManipulator simpleVolumeManipulator ){
        this.simpleVolumeManipulator = simpleVolumeManipulator;
    }

    @Override
    public List<Volume> getChildren() {
        return super.getChildren();
    }

    @Override
    public void channelExport( KOMFileSystem fileSystem, FileNode file ) throws IOException {
        List<GUID> physicalVolumes = this.lsblk();
        PhysicalVolume physicalVolume = this.volumeTree.getPhysicalVolume(physicalVolumes.get(0));
        physicalVolume.channelExport( fileSystem,file );
    }

    @Override
    public void streamExport( KOMFileSystem fileSystem, FileNode file ) throws IOException {
        List<GUID> physicalVolumes = this.lsblk();
        PhysicalVolume physicalVolume = this.volumeTree.getPhysicalVolume(physicalVolumes.get(0));
        physicalVolume.streamExport( fileSystem, file );
    }

    @Override
    public void channelReceive(KOMFileSystem fileSystem, FileNode file, FileChannel channel) throws IOException {
        List<GUID> physicalVolumes = this.lsblk();
        PhysicalVolume physicalVolume = this.volumeTree.getPhysicalVolume(physicalVolumes.get(0));
        physicalVolume.channelReceive( fileSystem, file, channel );
    }

    @Override
    public void extendLogicalVolume(GUID physicalGuid) {
        this.simpleVolumeManipulator.extendLogicalVolume( this.guid, physicalGuid );
    }

    @Override
    public List<GUID> lsblk() {
        return this.simpleVolumeManipulator.lsblk( this.guid );
    }

    @Override
    public void streamReceive(KOMFileSystem fileSystem, FileNode file, InputStream inputStream) throws IOException {
        List<GUID> physicalVolumes = this.lsblk();
        PhysicalVolume physicalVolume = this.volumeTree.getPhysicalVolume(physicalVolumes.get(0));
        physicalVolume.streamReceive( fileSystem,file,inputStream );
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
