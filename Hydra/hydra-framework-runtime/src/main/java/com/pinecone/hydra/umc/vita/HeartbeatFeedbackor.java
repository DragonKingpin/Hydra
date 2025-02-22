package com.pinecone.hydra.umc.vita;

import java.io.IOException;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.UMCMessage;

public interface HeartbeatFeedbackor extends Pinenut {
    boolean interceptHeartbeat( ChannelControlBlock block, UMCMessage msg ) throws IOException ;

    void feedback( ChannelControlBlock block, UMCMessage msg ) throws IOException ;
}
