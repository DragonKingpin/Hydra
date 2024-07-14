package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.ArchUMCMessage;
import com.pinecone.hydra.umc.msg.UMCHead;

import java.io.InputStream;
import java.util.Map;

public class UlfMCMessage extends ArchUMCMessage {
    public UlfMCMessage( UMCHead head ) {
        super(head);
    }

    public UlfMCMessage( UMCHead head, byte[] sBytesBody   ) {
        super( head, sBytesBody );
    }

    public UlfMCMessage( UMCHead head, String szStringBody ) {
        super( head, szStringBody );
    }

    public UlfMCMessage( UMCHead head, InputStream inStream ) {
        super( head, inStream );
    }


    public UlfMCMessage( Map<String,Object > joExHead ) {
        super( joExHead );
    }

    public UlfMCMessage( Map<String,Object > joExHead, byte[] sBytesBody ) {
        super( joExHead, sBytesBody );
    }

    public UlfMCMessage( Map<String,Object > joExHead, String szStringBody ) {
        super( joExHead, szStringBody );
    }

    public UlfMCMessage( Map<String,Object > joExHead, InputStream inStream ) {
        super( joExHead, inStream );
    }
}
