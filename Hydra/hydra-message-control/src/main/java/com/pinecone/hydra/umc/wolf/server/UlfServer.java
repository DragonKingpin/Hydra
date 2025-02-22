package com.pinecone.hydra.umc.wolf.server;

import com.pinecone.hydra.umc.msg.Recipient;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.wolf.UlfMessageNode;
import com.pinecone.hydra.umc.wolf.WolfMCNode;
import com.pinecone.hydra.umct.UMCTExpressHandler;

public interface UlfServer extends UlfMessageNode, Recipient {
    WolfMCNode apply( UMCTExpressHandler handler );

    UlfServer registerDataArrivedEventHandlers( ChannelEventHandler handler ) throws IllegalStateException;

    UlfServer deregisterDataArrivedEventHandlers( ChannelEventHandler handler ) throws IllegalStateException;
}
