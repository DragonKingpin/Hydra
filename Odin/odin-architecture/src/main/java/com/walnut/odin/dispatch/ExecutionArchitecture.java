package com.walnut.odin.dispatch;

public enum ExecutionArchitecture {
    JVM,
    NATIVE,
    SCRIPT,
    ANY;

    public static ExecutionArchitecture fromName( String szName ) {
        if ( szName == null ) {
            return ANY;
        }

        String szNormalized = szName.trim().toUpperCase();
        if ( szNormalized.isEmpty() ) {
            return ANY;
        }

        for ( ExecutionArchitecture architecture : values() ) {
            if ( architecture.name().equals( szNormalized ) ) {
                return architecture;
            }
        }

        return ANY;
    }
}
