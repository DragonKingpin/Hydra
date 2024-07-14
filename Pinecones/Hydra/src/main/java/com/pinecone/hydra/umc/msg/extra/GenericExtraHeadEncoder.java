package com.pinecone.hydra.umc.msg.extra;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.umc.msg.ExtraEncode;
import com.pinecone.hydra.umc.msg.UMCHead;
import com.pinecone.ulf.util.bson.WolfJSONCompiler;
import com.pinecone.ulf.util.bson.WolfJSONDecompiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class GenericExtraHeadEncoder implements ExtraHeadEncoder {
    @Override
    public byte[] encode( UMCHead head, Map<String, Object> jo ) throws ExtraHeadMarshalingException {
        ExtraEncode encode = head.getExtraEncode();
        switch ( encode ) {
            case JSONString: {
                return JSON.stringify( jo ).getBytes();
            }
            case Binary: {
                WolfJSONCompiler compiler = new WolfJSONCompiler();
                ByteArrayOutputStream  os = new ByteArrayOutputStream();

                try{
                    compiler.compile( jo, os );
                }
                catch ( IOException e ) {
                    throw new ExtraHeadMarshalingException( e );
                }

                return os.toByteArray();
            }
        }

        throw new ExtraHeadMarshalingException( "Unsupported encode mode[" + encode.getName() + "]." );
    }
}
