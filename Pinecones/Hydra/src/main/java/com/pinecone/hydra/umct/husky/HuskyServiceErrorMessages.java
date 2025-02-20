package com.pinecone.hydra.umct.husky;

import com.pinecone.framework.util.Bytes;
import com.pinecone.hydra.umc.msg.Status;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.wolfmc.UlfInformMessage;

public final class HuskyServiceErrorMessages {
    
    public static final UMCMessage HCTP_INTERNAL_ERROR ;

    public static final UMCMessage HCTP_ILLEGAL_MESSAGE ;

    public static final UMCMessage HCTP_MAPPING_NOT_FOUND ;


    static {
        HCTP_INTERNAL_ERROR = new UlfInformMessage( Bytes.Empty );
        HCTP_INTERNAL_ERROR.getHead().setStatus( Status.InternalError );

        HCTP_ILLEGAL_MESSAGE = new UlfInformMessage( Bytes.Empty );
        HCTP_INTERNAL_ERROR.getHead().setStatus( Status.IllegalMessage );

        HCTP_MAPPING_NOT_FOUND = new UlfInformMessage( Bytes.Empty );
        HCTP_INTERNAL_ERROR.getHead().setStatus( Status.MappingNotFound );

    }
}
