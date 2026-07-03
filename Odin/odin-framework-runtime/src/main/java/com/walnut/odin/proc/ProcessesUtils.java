package com.walnut.odin.proc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.pinecone.framework.util.datetime.DatePattern;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcessStatus;
import com.walnut.odin.proc.RemoteTerminationStatus;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;

public final class ProcessesUtils {

    public static Map<String, String> decode( String json ) {
        Map<String, String> map = new HashMap<>();
        if ( json == null || json.isEmpty() ) {
            return map;
        }

        JSONObject jo = new JSONMaptron( json );
        for ( Map.Entry<String, Object> kv : jo.entrySet() ) {
            Object value = kv.getValue();
            map.put( kv.getKey(), value == null ? "" : value.toString() );
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

        UProcessStatus status = that.getStatus();
        meta.setStatus( status == null ? null : status.toString() );
        meta.setTerminated( that.isTerminated() );
        meta.setExitCode( that.actionTape().getExitCode() );
        Throwable lastError = that.actionTape().getLastError();
        if ( lastError != null ) {
            meta.setMessage( lastError.getMessage() );
            meta.setTerminationStatus( RemoteTerminationStatus.Error.name() );
        }
        else if ( that.isTerminated() ) {
            meta.setTerminationStatus( RemoteTerminationStatus.Expected.name() );
        }

        return meta;
    }

}

