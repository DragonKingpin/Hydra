package com.walnut.odin.dispatch;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONArray;

public final class ExecutionArchitects {

    public static final String DefaultExecCaps = "[\"ANY\"]";

    private ExecutionArchitects() {
    }

    public static String normalizeExecArch( String szExecArch ) {
        if ( szExecArch == null ) {
            return ExecutionArchitecture.ANY.name();
        }

        return ExecutionArchitecture.fromName( szExecArch ).name();
    }

    public static String normalizeExecCaps( String szExecCaps ) {
        if ( szExecCaps == null || szExecCaps.trim().isEmpty() ) {
            return DefaultExecCaps;
        }
        return szExecCaps.trim();
    }

    public static boolean canRun( String szExecCaps, String szExecArch ) {
        String szTargetArch = normalizeExecArch( szExecArch );
        return containsCap( szExecCaps, szTargetArch ) || containsCap( szExecCaps, ExecutionArchitecture.ANY.name() );
    }

    protected static boolean containsCap( String szExecCaps, String szCap ) {
        String szNormalizedCaps = normalizeExecCaps( szExecCaps );
        try {
            Object parsed = JSON.parse( szNormalizedCaps );
            if ( parsed instanceof JSONArray ) {
                JSONArray caps = (JSONArray) parsed;
                for ( Object cap : caps ) {
                    if ( szCap.equals( normalizeExecArch( String.valueOf( cap ) ) ) ) {
                        return true;
                    }
                }
                return false;
            }
        }
        catch ( RuntimeException ignored ) {
        }

        String[] parts = szNormalizedCaps.split( "[,;|\\[\\]\\\"'\\s]+" );
        for ( String part : parts ) {
            if ( szCap.equals( normalizeExecArch( part ) ) ) {
                return true;
            }
        }
        return false;
    }
}
