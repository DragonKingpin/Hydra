package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.prototype.Pinenut;

public class ChannelAllocateException extends RuntimeException implements Pinenut {
    public ChannelAllocateException() {
        super();
    }

    public ChannelAllocateException( String message ) {
        super(message);
    }

    public ChannelAllocateException( String message, Throwable cause ) {
        super(message, cause);
    }

    public ChannelAllocateException( Throwable cause ) {
        super(cause);
    }
}