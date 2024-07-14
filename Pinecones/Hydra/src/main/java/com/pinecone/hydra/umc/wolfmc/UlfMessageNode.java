package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.ChannelPool;
import com.pinecone.hydra.umc.msg.MessageNode;

public interface UlfMessageNode extends MessageNode {
    ChannelPool          getChannelPool();
}
