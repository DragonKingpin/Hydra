package com.walnut.odin.dispatch;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONArray;

public final class ExecutionArchitects {

    private static final Logger log = LoggerFactory.getLogger( ExecutionArchitects.class );

    private ExecutionArchitects() {
    }

    public static String normalizeExecArch( String szExecArch ) {
        return ExecutionArchitecture.fromName( szExecArch ).name();
    }

    public static String normalizeExecCaps( String szExecCaps ) {
        return JSON.stringify( parseExecCaps( szExecCaps ) );
    }

    public static List<String> parseExecCaps( String szExecCaps ) {
        if ( szExecCaps == null || szExecCaps.trim().isEmpty() ) {
            throw new IllegalArgumentException( "Execution capabilities must be an explicit JSON array." );
        }
        Object parsed;
        try {
            parsed = JSON.parse( szExecCaps );
        }
        catch ( RuntimeException e ) {
            throw new IllegalArgumentException( "Invalid execution capabilities JSON array.", e );
        }
        if ( !( parsed instanceof JSONArray ) ) {
            throw new IllegalArgumentException( "Execution capabilities must be a JSON array." );
        }
        Set<String> caps = new LinkedHashSet<>();
        for ( Object cap : (JSONArray) parsed ) {
            if ( !( cap instanceof String ) ) {
                throw new IllegalArgumentException( "Each execution capability must be a string." );
            }
            caps.add( normalizeExecArch( (String) cap ) );
        }
        return new ArrayList<>( caps );
    }

    public static boolean canRun( String szExecCaps, String szExecArch ) {
        try {
            String target = normalizeExecArch( szExecArch );
            // Validate the entire set before matching, including entries after a match.
            return parseExecCaps( szExecCaps ).contains( target );
        }
        catch ( IllegalArgumentException e ) {
            log.warn( "Execution capability match rejected: {}", e.getMessage() );
            return false;
        }
    }
}
