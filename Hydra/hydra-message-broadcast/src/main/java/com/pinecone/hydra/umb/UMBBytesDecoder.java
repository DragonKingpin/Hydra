package com.pinecone.hydra.umb;

import java.io.IOException;

import com.pinecone.hydra.umc.msg.GenericEMCBytesDecoder;
import com.pinecone.hydra.umc.msg.UMCHead;
import com.pinecone.hydra.umc.msg.UMCProtocolMagic;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;

public class UMBBytesDecoder extends GenericEMCBytesDecoder {
    @Override
    public UMCHead decode( byte[] buf, ExtraHeadCoder extraHeadCoder ) throws IOException {
        if ( !UMCProtocolMagic.isUniformMessage( buf ) ) {
            return null;
        }

        if ( UMCProtocolMagic.variantOf( buf ) == UMCProtocolMagic.VARIANT_UMBP ) {
            return UMBPHeadV1.decode( buf, UMBPHeadV1.ProtocolSignature, extraHeadCoder );
        }

        return super.decode( buf, extraHeadCoder );
    }
}
