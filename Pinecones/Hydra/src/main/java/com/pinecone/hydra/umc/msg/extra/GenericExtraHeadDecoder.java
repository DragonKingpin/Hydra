package com.pinecone.hydra.umc.msg.extra;

import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.umc.msg.ExtraEncode;
import com.pinecone.hydra.umc.msg.UMCHead;
import com.pinecone.ulf.util.bson.WolfJSONDecompiler;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class GenericExtraHeadDecoder implements ExtraHeadDecoder {
    @Override
    @SuppressWarnings( "unchecked" )
    public Map<String, Object > decode( UMCHead head, byte[] raw ) {
        ExtraEncode encode = head.getExtraEncode();
        switch ( encode ) {
            case JSONString: {
                JSONObject jo = new JSONMaptron( head.getExtraHead(), true );
                jo.jsonDecode( new String( raw ) );
                return jo;
            }
            case Binary: {
                ByteArrayInputStream       is = new ByteArrayInputStream( raw );
                WolfJSONDecompiler decompiler = new WolfJSONDecompiler( is );

                Object o = decompiler.decompile();
                if( o instanceof JSONObject ) {
                    return (JSONObject) o;
                }
                else if( o instanceof Map ) {
                    return new JSONMaptron( (Map<String, Object >)o, true ) ;
                }

                throw new ExtraHeadMarshalingException(
                        "Illegal decompiler Binary json, requires Map<String, Object > but " + o.getClass().getSimpleName() + " found."
                );
            }
        }

        throw new ExtraHeadMarshalingException( "Unsupported encode mode[" + encode.getName() + "]." );
    }
}
