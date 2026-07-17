package com.pinecone.hydra.umc.wolf;

import java.util.Map;

import com.pinecone.hydra.umc.msg.ArchBytesTransferMessage;
import com.pinecone.hydra.umc.msg.ExtraEncode;
import com.pinecone.hydra.umc.msg.UMCHead;

public class UlfBytesTransferMessage extends ArchBytesTransferMessage {
    public UlfBytesTransferMessage( UMCHead head ) {
        super( head );
    }

    public UlfBytesTransferMessage( UMCHead head, byte[] sBytesBody   ) {
        super( head, sBytesBody );
    }

    public UlfBytesTransferMessage( UMCHead head, String szStringBody ) {
        this( head, szStringBody.getBytes() );
    }

    public UlfBytesTransferMessage( Map<String,Object > joExHead, byte[] sBytesBody, int controlBits ) {
        super( joExHead, sBytesBody, controlBits );
    }

    public UlfBytesTransferMessage( Map<String,Object > joExHead, String szStringBody, int controlBits ) {
        this( joExHead, szStringBody.getBytes(), controlBits );
    }

    public UlfBytesTransferMessage( Map<String,Object > joExHead, byte[] sBytesBody ) {
        this( joExHead, sBytesBody, 0 );
    }

    public UlfBytesTransferMessage( Map<String,Object > joExHead, String szStringBody ) {
        this( joExHead, szStringBody.getBytes(), 0 );
    }


    public UlfBytesTransferMessage( Object exHead, ExtraEncode encode, byte[] sBytesBody, int controlBits ) {
        super( exHead, encode, sBytesBody, controlBits );
    }

    public UlfBytesTransferMessage( Object exHead, ExtraEncode encode, String szStringBody, int controlBits ) {
        this( exHead, encode, szStringBody.getBytes(), controlBits );
    }

    public UlfBytesTransferMessage( Object exHead, byte[] sBytesBody ) {
        this( exHead, ExtraEncode.Blob, sBytesBody, 0 );
    }

    public UlfBytesTransferMessage( Object exHead, String szStringBody ) {
        this( exHead, ExtraEncode.Blob, szStringBody, 0 );
    }

}
