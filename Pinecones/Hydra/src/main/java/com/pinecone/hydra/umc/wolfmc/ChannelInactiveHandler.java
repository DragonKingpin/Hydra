package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;

public interface ChannelInactiveHandler extends Pinenut {
    boolean afterChannelInactive( ChannelControlBlock ccb ) throws ChannelHandleException;
}
