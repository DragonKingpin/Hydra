package com.pinecone.hydra.file.transmit;

import java.io.IOException;
import java.nio.channels.FileChannel;

public interface ChannelReceiverEntity extends ReceiveEntity{
    FileChannel getChannel();

    void setChannel( FileChannel channel );

    void receive( )throws IOException;
}
