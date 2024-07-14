package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.wolfmc.client.WolfMCClient;
import io.netty.channel.ChannelHandlerContext;

/**
 * UnsetUlfAsyncMsgHandleAdapter
 * Dummy UlfAsyncMsgHandleAdapter
 */
public final class UnsetUlfAsyncMsgHandleAdapter implements UlfAsyncMsgHandleAdapter {
    private MessageNode mMessageNode;

    public UnsetUlfAsyncMsgHandleAdapter( MessageNode node ) {
        this.mMessageNode = node;
    }

    @Override
    public void onSuccessfulMsgReceived(Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) {
        this.mMessageNode.getSystem().console().warn( "Warning, MsgHandleAdapter is unset.", block.getChannel().getChannelID(), msg );
    }

    @Override
    public void onErrorMsgReceived( Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) {
        this.mMessageNode.getSystem().console().warn( "Warning, MsgHandleAdapter is unset.", block.getChannel().getChannelID(), msg );
    }

    @Override
    public void onError( ChannelHandlerContext ctx, Throwable cause ) {
        if( cause instanceof Exception ) {
            this.mMessageNode.getSystem().console().warn( cause.getStackTrace() );
            this.mMessageNode.getSystem().handleLiveException( (Exception) cause );
        }
        else {
            throw new ProvokeHandleException( cause );
        }
    }
}