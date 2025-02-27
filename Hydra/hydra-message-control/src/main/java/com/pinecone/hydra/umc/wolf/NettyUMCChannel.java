package com.pinecone.hydra.umc.wolf;

import com.pinecone.hydra.umc.msg.UMCChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

public interface NettyUMCChannel extends UMCChannel {
    @Override
    Channel        getNativeHandle();

    @Override
    ChannelId      getChannelID() ;
}
