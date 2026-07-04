package com.pinecone.hydra.umc.wolf;

import java.io.InputStream;
import java.util.Map;

import com.pinecone.hydra.umc.msg.ArchStreamTransferMessage;
import com.pinecone.hydra.umc.msg.ExtraEncode;
import com.pinecone.hydra.umc.msg.UMCHead;

public class UlfStreamTransferMessage extends ArchStreamTransferMessage {
    public UlfStreamTransferMessage( UMCHead head ) {
        super( head );
    }

    public UlfStreamTransferMessage( UMCHead head, InputStream inStream ) {
        super( head, inStream );
    }

    public UlfStreamTransferMessage( Map<String,Object > joExHead, InputStream inStream, int controlBits ) {
        super( joExHead, inStream, controlBits );
    }

    public UlfStreamTransferMessage( Map<String,Object > joExHead, InputStream inStream ) {
        super( joExHead, inStream, 0 );
    }

    public UlfStreamTransferMessage(Object exHead, ExtraEncode encode, InputStream inStream, int controlBits ) {
        super( exHead, encode, inStream, controlBits );
    }

    public UlfStreamTransferMessage( Object exHead, InputStream inStream ) {
        this( exHead, ExtraEncode.Blob, inStream, 0 );
    }
}
