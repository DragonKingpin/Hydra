package com.pinecone.hydra.umct;

import java.io.IOException;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelPool;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.wolfmc.UlfAsyncMsgHandleAdapter;
import com.pinecone.hydra.umct.appoint.AsynMsgHandler;

public interface DuplexExpress extends UMCTExpress {

    void afterChannelInactive( ChannelControlBlock controlBlock ) ;

    ChannelPool getPoolByClientId( long clientId ) ;

    void sendAsynMsg( long clientId, UMCMessage request, boolean bNoneBuffered, UlfAsyncMsgHandleAdapter handler ) throws IOException;

    void sendAsynMsg( long clientId, UMCMessage request, boolean bNoneBuffered, AsynMsgHandler handler ) throws IOException;


}
