package com.walnut.odin.proc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.pinecone.framework.util.datetime.DatePattern;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;

public final class ProcessesUtils {

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

    private static String formatTime( LocalDateTime time ) {
        if ( time == null ) {
            return null;
        }
        DateTimeFormatter formatter = DatePattern.createFormatter( "yyyy-MM-dd HH:mm:ss.nnnnnnnnn" );
        return time.format( formatter );
    }

    public static UProcessRuntimeMeta extractProcessMeta( UProcess that ) {
        UProcessRuntimeMeta meta = new UProcessRuntimeMeta();
        meta.setPID( that.getPID().toString() );
        GUID parentPID = that.actualParentPID();
        if ( parentPID == null ) {
            parentPID = that.getParentProcessId();
        }
        meta.setParentPID( parentPID == null ? null : parentPID.toString() );
        meta.setName( that.getName() );
        meta.setLocalPID( that.getLocalPID() );

        meta.setCreateTime( formatTime( that.getCreateTime() ) );
        meta.setStartTime( formatTime( that.getStartTime() ) );
        meta.setEndTime( formatTime( that.getEndTime() ) );
        meta.setLastUpdateTime( formatTime( that.getLastUpdateTime() ) );

        meta.setStatus( that.getStatus().toString() );
        meta.setTerminated( that.isTerminated() );

        return meta;
    }

}
