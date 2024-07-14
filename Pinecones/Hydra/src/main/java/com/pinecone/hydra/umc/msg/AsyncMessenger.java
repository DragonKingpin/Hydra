package com.pinecone.hydra.umc.msg;

import com.pinecone.hydra.umc.wolfmc.UlfAsyncMsgHandleAdapter;

import java.io.IOException;

public interface AsyncMessenger extends Messenger {
    void sendAsynMsg( UMCMessage request, boolean bNoneBuffered ) throws IOException;

    void sendAsynMsg( UMCMessage request, boolean bNoneBuffered, UlfAsyncMsgHandleAdapter handler ) throws IOException;

    // Javascript/Ajax style.
    default void sendAsynMsg( UMCMessage request, UlfAsyncMsgHandleAdapter handler ) throws IOException {
        this.sendAsynMsg( request, false, handler );
    }
}
