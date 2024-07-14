package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.AsyncMsgHandleAdapter;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCMessage;

import io.netty.channel.ChannelHandlerContext;
import com.pinecone.framework.util.Debug;

public interface UlfAsyncMsgHandleAdapter extends AsyncMsgHandleAdapter {
    default void onSuccessfulMsgReceived( Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) throws Exception {
        Debug.warn( block.getChannel().getChannelID(), msg );
    }

    default void onErrorMsgReceived( Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) throws Exception {
        Debug.warn( block.getChannel().getChannelID(), msg.getHead() );
    }

    default void onError( ChannelHandlerContext ctx, Throwable cause ) {

    }

}