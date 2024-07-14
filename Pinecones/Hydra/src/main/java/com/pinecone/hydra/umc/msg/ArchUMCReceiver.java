package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.ProvokeHandleException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class ArchUMCReceiver extends ArchUMCProtocol implements UMCReceiver {
    public ArchUMCReceiver( Medium messageSource ) {
        super( messageSource );
    }

    public Map<String, Object> readPutMsg() throws IOException {
        UMCHead head = this.readMsgHead();
        if( head.method != UMCMethod.PUT ) {
            throw new IOException( "[UMCProtocol] Illegal protocol method." );
        }
        return head.getExtraHead();
    }

    protected UMCHead readPostHead() throws IOException {
        UMCHead head = this.readMsgHead();
        if( head.method != UMCMethod.POST ) {
            throw new IOException( "[UMCProtocol] Illegal protocol method." );
        }
        return head;
    }

    protected void onlyReadPostBody( ArchUMCMessage message, boolean bAllBytes ) throws IOException {
        if( bAllBytes ) {
            message.setBody( this.mInputStream.readAllBytes() );
        }
        else {
            message.setBody( this.mInputStream );
        }
    }

    public UMCMessage readMsg( boolean bAllBytes, Class<? extends ArchUMCMessage > stereotype ) throws IOException {
        try{
            UMCHead head = this.readMsgHead();
            ArchUMCMessage message = stereotype.getConstructor( UMCHead.class ).newInstance( head );
            if( head.getMethod() == UMCMethod.POST ){
                this.onlyReadPostBody( message, bAllBytes );
            }
            else {
                if( head.getMethod() != UMCMethod.PUT ){
                    throw new IOException( " [UMCProtocol] Unrecognized protocol method." );
                }
            }

            return message;
        }
        catch ( NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e ) {
            throw new ProvokeHandleException( e );
        }
    }
}
