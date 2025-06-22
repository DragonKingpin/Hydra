package com.walnut.odin.proc;

import java.util.HashMap;
import java.util.Map;

import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;

public final class ArgumentsUtils {

    public static Map<String, String[]> decode( String json ) {
        Map<String, String[]> map = new HashMap<>();
        if ( json == null || json.isEmpty() ) {
            return map;
        }

        JSONObject jo = new JSONMaptron( json );
        for ( Map.Entry<String, Object> kv : jo.entrySet() ) {
            JSONArray ja = (JSONArray) kv.getValue();
            String[] vs = new String[ ja.size() ];
            for ( int i = 0; i < ja.size(); ++i ) {
                vs[ i ] = ja.optString( i );
            }

            map.put( kv.getKey(), vs );
        }

        return map;
    }

}
