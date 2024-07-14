package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.ArchUMCReceiver;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCHead;
import com.pinecone.hydra.umc.msg.ArchUMCMessage;
import com.pinecone.hydra.umc.msg.UMCMessage;

import java.io.IOException;

public class UlfMCReceiver extends ArchUMCReceiver {
    public UlfMCReceiver( Medium messageSource ) {
        super( messageSource );
    }

    public UMCMessage readPostMsg(boolean bAllBytes ) throws IOException {
        UMCHead head = this.readPostHead();
        ArchUMCMessage message = new UlfMCMessage( head );
        this.onlyReadPostBody( message, bAllBytes );
        return message;
    }

    @Override
    public UMCMessage readPostMsg() throws IOException {
        return this.readPostMsg( false );
    }

    @Override
    public UMCMessage readPostMsgBytes() throws IOException {
        return this.readPostMsg( true );
    }

    @Override
    public UMCMessage readMsg() throws IOException {
        return this.readMsg( false, UlfMCMessage.class );
    }

    @Override
    public UMCMessage readMsgBytes() throws IOException {
        return this.readMsg( true, UlfMCMessage.class );
    }
}
