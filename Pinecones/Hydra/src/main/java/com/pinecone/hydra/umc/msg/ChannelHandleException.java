package com.pinecone.hydra.umc.msg;

import java.io.IOException;

import com.pinecone.framework.system.prototype.Pinenut;

public class ChannelHandleException extends IOException implements Pinenut {
    public ChannelHandleException() {
        super();
    }

    public ChannelHandleException( String message ) {
        super(message);
    }

    public ChannelHandleException( String message, Throwable cause ) {
        super(message, cause);
    }

    public ChannelHandleException( Throwable cause ) {
        super(cause);
    }
}

