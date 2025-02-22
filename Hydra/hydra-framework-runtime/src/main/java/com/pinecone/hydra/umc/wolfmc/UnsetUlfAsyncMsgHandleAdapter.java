package com.pinecone.hydra.umc.wolfmc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.UMCReceiver;
import com.pinecone.hydra.umc.msg.UMCTransmit;

import io.netty.channel.ChannelHandlerContext;

/**
 * UnsetUlfAsyncMsgHandleAdapter
 * Dummy UlfAsyncMsgHandleAdapter
 */
public final class UnsetUlfAsyncMsgHandleAdapter implements UlfAsyncMsgHandleAdapter {
    private MessageNode mMessageNode;
    private Logger      mLogger;

    public UnsetUlfAsyncMsgHandleAdapter( MessageNode node ) {
        this.mMessageNode = node;

        if ( this.mMessageNode instanceof Slf4jTraceable ) {
            this.mLogger = ((Slf4jTraceable) this.mMessageNode).getLogger();
        }
        else {
            this.mLogger = LoggerFactory.getLogger( this.getClass() );
        }
    }

    @Override
    public void onSuccessfulMsgReceived( Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) {
        this.mLogger.warn( "Warning, MsgHandleAdapter is unset. Info => {}, {}", block.getChannel().getChannelID(), msg );
    }

    @Override
    public void onSuccessfulMsgReceived( Medium medium, UMCTransmit transmit, UMCReceiver receiver, UMCMessage msg, Object[] args ) throws Exception {
        this.mLogger.warn( "Warning, MsgHandleAdapter is unset. Info => {}", msg );
    }

    @Override
    public void onErrorMsgReceived( Medium medium, UMCTransmit transmit, UMCReceiver receiver, UMCMessage msg, Object[] args ) throws Exception {
        this.mLogger.warn( "Warning, MsgHandleAdapter is unset. Info => {}", msg );
    }

    @Override
    public void onErrorMsgReceived( Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) {
        this.mLogger.warn( "Warning, MsgHandleAdapter is unset. Info => {}", msg );
    }

    @Override
    public void onError( ChannelHandlerContext ctx, Throwable cause ) {
        this.onError( (Object) ctx, cause );
    }

    @Override
    public void onError( Object data, Throwable cause ) {
        this.mLogger.error( "UnsetMsgHandleAdapter. Error => {}, {}", cause.getMessage(), cause.toString() );
        if( !( cause instanceof Exception ) ) {
            throw new ProvokeHandleException( cause );
        }
    }
}