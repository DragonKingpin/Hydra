package com.walnut.odin.dispatch;

import java.util.Locale;

public enum ExecutionArchitecture {
    NATIVE_JAR,
    PY_ARTIFACT,
    NATIVE,
    SCRIPT,
    GENERIC_IMAGE,
    SPARK_JAR,
    HIVE_SQL;

    public static ExecutionArchitecture fromName( String szName ) {
        if ( szName == null ) {
            throw new IllegalArgumentException( "Execution architecture is required." );
        }

        String szNormalized = szName.trim().toUpperCase( Locale.ROOT );
        if ( szNormalized.isEmpty() ) {
            throw new IllegalArgumentException( "Execution architecture is required." );
        }

        for ( ExecutionArchitecture architecture : values() ) {
            if ( architecture.name().equals( szNormalized ) ) {
                return architecture;
            }
        }

        throw new IllegalArgumentException( "Unsupported execution architecture: " + szName );
    }
}
