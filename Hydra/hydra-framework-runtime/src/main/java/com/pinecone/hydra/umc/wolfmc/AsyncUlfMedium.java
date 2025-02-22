package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.io.ChannelInputStream;
import com.pinecone.hydra.umc.io.ChannelOutputStream;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.MessageNode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.InputStream;
import java.io.OutputStream;

public class AsyncUlfMedium implements Medium {
    protected ChannelHandlerContext mContext;

    protected ByteBuf               mInBuf;

    protected OutputStream          mOutputStream ;

    protected InputStream           mInputStream;

    protected MessageNode           mMessageNode;

    public AsyncUlfMedium( ChannelHandlerContext context, MessageNode messageNode ) {
        this.mContext       = context;
        this.mInBuf         = null;
        this.mOutputStream  = new ChannelOutputStream( this.mContext );
        this.mInputStream   = null;
        this.mMessageNode   = messageNode;
    }

    public AsyncUlfMedium( ChannelHandlerContext context, ByteBuf byteBuf, MessageNode messageNode ) {
        this.mContext       = context;
        this.mInBuf         = byteBuf;
        this.mOutputStream  = new ChannelOutputStream( this.mContext );
        this.mInputStream   = new ChannelInputStream( this.mInBuf );
        this.mMessageNode   = messageNode;
    }

    @Override
    public OutputStream getOutputStream(){
        return this.mOutputStream;
    }

    @Override
    public InputStream getInputStream(){
        return this.mInputStream;
    }

    @Override
    public Object getNativeMessageSource(){
        return this.mContext.channel();
    }

    @Override
    public String sourceName(){
        return "WolfUMC";
    }

    @Override
    public MessageNode getMessageNode() {
        return this.mMessageNode;
    }

    @Override
    public void release() {
        this.mContext        = null;
        this.mInBuf          = null;
        this.mOutputStream   = null;
        this.mInputStream    = null;
    }
}
