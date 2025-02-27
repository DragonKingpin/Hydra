package com.pinecone.hydra.umc.wolf.client;

import java.io.IOException;

import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.wolf.UlfAsyncMsgHandleAdapter;
import com.pinecone.hydra.umc.wolf.UlfMessageNode;

public interface UlfClient extends UlfMessageNode {

    ClientConnectArguments getConnectionArguments();

    UMCMessage sendSyncMsg( UMCMessage request ) throws IOException;

    UMCMessage sendSyncMsg( UMCMessage request, boolean bNoneBuffered ) throws IOException ;

    void       sendAsynMsg( UMCMessage request ) throws IOException ;

    void       sendAsynMsg( UMCMessage request, UlfAsyncMsgHandleAdapter handler ) throws IOException;

    UlfClient  registerChannelConnectedHandler  ( ChannelEventHandler handler ) throws IllegalStateException ;

    UlfClient  deregisterChannelConnectedHandler( ChannelEventHandler handler ) throws IllegalStateException ;

}
