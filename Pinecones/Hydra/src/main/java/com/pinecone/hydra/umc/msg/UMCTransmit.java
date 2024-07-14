package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.util.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public interface UMCTransmit extends UMCProtocol{
    void sendPutMsg( JSONObject msg ) throws IOException;

    void sendPutMsg( JSONObject msg, Status status ) throws IOException;

    void sendPostMsg( JSONObject msg, byte[] bytes ) throws IOException;

    void sendPostMsg( JSONObject msg, byte[] bytes, Status status ) throws IOException;

    default void sendPostMsg( JSONObject msg, String sz ) throws IOException {
        this.sendPostMsg( msg, sz.getBytes() );
    }

    void sendPostMsg( JSONObject msg, InputStream is ) throws IOException;

    void sendMsg( UMCMessage msg, boolean bNoneBuffered ) throws IOException;

    default void sendMsg( UMCMessage msg ) throws IOException {
        this.sendMsg( msg, false );
    }
}
