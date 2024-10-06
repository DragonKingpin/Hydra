package com.pinecone.hydra.umc.msg.extra;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.umc.msg.ExtraEncode;
import com.pinecone.hydra.umc.msg.UMCHead;
import com.pinecone.ulf.util.bson.WolfJSONCompiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GenericExtraHeadEncoder implements ExtraHeadEncoder {
    @Override
    public byte[] encode( UMCHead head, Object raw ) throws ExtraHeadMarshalingException {
        ExtraEncode encode = head.getExtraEncode();
        switch ( encode ) {
            case JSONString: {
                return JSON.stringify( raw ).getBytes();
            }
            case Binary: {
                WolfJSONCompiler compiler = new WolfJSONCompiler();
                ByteArrayOutputStream  os = new ByteArrayOutputStream();

                try{
                    compiler.compile( raw, os );
                }
                catch ( IOException e ) {
                    throw new ExtraHeadMarshalingException( e );
                }

                return os.toByteArray();
            }
            case Prototype: {
                return (byte[]) raw;
            }
        }

        throw new ExtraHeadMarshalingException( "Unsupported encode mode[" + encode.getName() + "]." );
    }
}
