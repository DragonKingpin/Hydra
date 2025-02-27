package com.pinecone.hydra.umc.vita;

import java.io.IOException;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.UMCMessage;

public interface HeartbeatControl extends Pinenut {

    void registerChannels( Collection<ChannelControlBlock> channels, long intervalMillis ) ;

    void registerChannel( ChannelControlBlock ccb, long intervalMillis ) ;

    void deregisterChannel( ChannelControlBlock ccb ) ;

    void shutdown() ;

    boolean interceptFeedback( ChannelControlBlock block, UMCMessage msg ) throws IOException;

}
