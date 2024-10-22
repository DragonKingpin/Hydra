package com.pinecone.hydra.storage.volume.entity.local;

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
import com.pinecone.hydra.storage.volume.entity.Volume;
import com.pinecone.hydra.storage.volume.source.SimpleVolumeManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;

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
        File temporaryFile = new File(this.mountPoint.getMountPoint());
        FileChannel channel = FileChannel.open(temporaryFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        GenericChannelExporterEntity exporterEntity = new GenericChannelExporterEntity(fileSystem, file, channel);
        exporterEntity.export();
    }

    @Override
    public void streamExport( KOMFileSystem fileSystem, FileNode file ) throws IOException {
        File temporaryFile = new File(this.mountPoint.getMountPoint());
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile);
        GenericStreamExporterEntity exporterEntity = new GenericStreamExporterEntity(fileSystem, file, fileOutputStream);
        exporterEntity.export();
    }

    @Override
    public void channelReceiver(KOMFileSystem fileSystem, FileNode file, FileChannel channel) throws IOException {
        GenericChannelReceiveEntity receiveEntity = new GenericChannelReceiveEntity(fileSystem, this.mountPoint.getMountPoint(), file, channel);
        receiveEntity.receive();
    }

    @Override
    public void streamReceiver(KOMFileSystem fileSystem, FileNode file, InputStream inputStream) throws IOException {
        GenericStreamReceiverEntity receiverEntity = new GenericStreamReceiverEntity(fileSystem, this.mountPoint.getMountPoint(), file, inputStream);
        receiverEntity.receive();
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
