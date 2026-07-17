package com.pinecone.hydra.umc.msg;

import java.io.IOException;

import com.pinecone.framework.util.json.JSONException;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;

public class GenericEMCBytesDecoder implements EMCBytesDecoder {
    @Override
    public UMCHead decode( byte[] buf, ExtraHeadCoder extraHeadCoder ) throws IOException {
        if ( !UMCProtocolMagic.isUniformMessage( buf ) ) {
            return null;
        }

        switch ( UMCProtocolMagic.variantOf( buf ) ) {
            case UMCProtocolMagic.VARIANT_UMC: {
                return UMCHeadV1.decode( buf, UMCHeadV1.ProtocolSignature, extraHeadCoder );
            }
            case UMCProtocolMagic.VARIANT_UMCC: {
                return UMCCHeadV1.decode( buf, UMCCHeadV1.ProtocolSignature, extraHeadCoder );
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public UMCHead decodeIntegrated( byte[] buf, ExtraHeadCoder extraHeadCoder ) throws IOException {
        UMCHead head = this.decode( buf, extraHeadCoder );

        byte[] headBuf = new byte[ head.getExtraHeadLength() ];
        int headSize = head.sizeof();
        System.arraycopy( buf, headSize, headBuf, 0, head.getExtraHeadLength() );

        if ( buf.length < head.getExtraHeadLength() ) {
            throw new StreamTerminateException("[UMCProtocol] Buffer is not long enough.");
        }

        try {
            Object jo = extraHeadCoder.getDecoder().decode( head, headBuf );
            AbstractUMCHead.setExtraHeadExplicitly( head, jo );
        }
        catch ( JSONException e ) {
            throw new IOException(" [UMCProtocol] Illegal protocol head.");
        }

        return head;
    }
}
