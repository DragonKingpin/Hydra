package com.pinecone.hydra.file.transmit;

import java.nio.channels.FileChannel;

public interface ChannelReceiverEntity extends ReceiveEntity{
    FileChannel getChannel();
    void setChannel( FileChannel channel );
    void receive( );
}
