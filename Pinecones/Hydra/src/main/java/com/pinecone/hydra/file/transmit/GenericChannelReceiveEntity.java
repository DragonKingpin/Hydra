package com.pinecone.hydra.file.transmit;

import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.file.entity.FileNode;

import java.nio.channels.FileChannel;

public class GenericChannelReceiveEntity extends ArchReceiveEntity implements ChannelReceiverEntity{
    private FileChannel channel;
    private ChannelReceiver channelReceiver;

    public GenericChannelReceiveEntity(KOMFileSystem fileSystem, String destDirPath, FileNode file,FileChannel channel) {
        super(fileSystem, destDirPath, file);
        this.channel = channel;
        this.channelReceiver = new GenericChannelReceiver();
    }

    @Override
    public ChannelReceiverEntity evinceChannelReceiverEntity() {
        return this;
    }

    @Override
    public FileChannel getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(FileChannel channel) {
        this.channel = channel;
    }

    @Override
    public void receive() {
        this.channelReceiver.receive(this);
    }
}
