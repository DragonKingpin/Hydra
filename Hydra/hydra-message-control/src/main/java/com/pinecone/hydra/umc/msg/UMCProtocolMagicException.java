package com.pinecone.hydra.umc.msg;

import java.io.IOException;

import com.pinecone.framework.system.prototype.Pinenut;

public class UMCProtocolMagicException extends IOException implements Pinenut {
    public UMCProtocolMagicException( String message ) {
        super( message );
    }

    public UMCProtocolMagicException( String message, Throwable cause ) {
        super( message, cause );
    }
}
