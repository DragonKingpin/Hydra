package com.pinecone.hydra.umc.msg.event;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCMessage;

import io.netty.channel.ChannelHandlerContext;

public interface ChannelDataInterceptor extends Pinenut {

    boolean interceptAfterDataArrived ( Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) throws ChannelHandleException;

}