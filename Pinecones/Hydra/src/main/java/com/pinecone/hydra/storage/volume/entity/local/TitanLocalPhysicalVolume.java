package com.pinecone.hydra.storage.volume.entity.local;

import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.transmit.exporter.channel.GenericChannelExporterEntity;
import com.pinecone.hydra.storage.file.transmit.exporter.stream.GenericStreamExporterEntity;
import com.pinecone.hydra.storage.file.transmit.receiver.channel.GenericChannelReceiveEntity;
import com.pinecone.hydra.storage.file.transmit.receiver.stream.GenericStreamReceiverEntity;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.ArchVolume;
import com.pinecone.hydra.storage.volume.entity.MountPoint;
import com.pinecone.hydra.storage.volume.entity.VolumeCapacity;
import com.pinecone.hydra.storage.volume.source.PhysicalVolumeManipulator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class TitanLocalPhysicalVolume extends ArchVolume implements LocalPhysicalVolume{
    private MountPoint                  mountPoint;
    private PhysicalVolumeManipulator   physicalVolumeManipulator;

    public TitanLocalPhysicalVolume(VolumeTree volumeTree, PhysicalVolumeManipulator physicalVolumeManipulator) {
        super(volumeTree);
        this.physicalVolumeManipulator = physicalVolumeManipulator;
    }


    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public void channelExport(KOMFileSystem fileSystem, FileNode file) throws IOException {
        File temporaryFile = new File(this.mountPoint.getMountPoint());
        FileChannel channel = FileChannel.open(temporaryFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        GenericChannelExporterEntity exporterEntity = new GenericChannelExporterEntity(fileSystem, file, channel);
        exporterEntity.export();
    }

    @Override
    public void streamExport(KOMFileSystem fileSystem, FileNode file) throws IOException {
        File temporaryFile = new File(this.mountPoint.getMountPoint());
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile);
        GenericStreamExporterEntity exporterEntity = new GenericStreamExporterEntity(fileSystem, file, fileOutputStream);
        exporterEntity.export();
    }

    @Override
    public void channelReceive(KOMFileSystem fileSystem, FileNode file, FileChannel channel) throws IOException {
        GenericChannelReceiveEntity receiveEntity = new GenericChannelReceiveEntity(fileSystem, this.mountPoint.getMountPoint(), file, channel);
        receiveEntity.receive();
    }

    @Override
    public void streamReceive(KOMFileSystem fileSystem, FileNode file, InputStream inputStream) throws IOException {
        GenericStreamReceiverEntity receiverEntity = new GenericStreamReceiverEntity(fileSystem, this.mountPoint.getMountPoint(), file, inputStream);
        receiverEntity.receive();
    }

    @Override
    public MountPoint getMountPoint() {
        return this.mountPoint;
    }

    @Override
    public void setMountPoint(MountPoint mountPoint) {
        this.mountPoint = mountPoint;
    }
}
