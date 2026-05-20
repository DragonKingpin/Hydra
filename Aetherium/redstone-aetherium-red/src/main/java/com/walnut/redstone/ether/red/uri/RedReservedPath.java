package com.walnut.redstone.ether.red.uri;

public final class RedReservedPath {
    public static final String Red = "/__red__";

    private RedReservedPath() {
    }

    public static boolean isReserved( String path ) {
        String value = path == null ? "" : path;
        return value.startsWith( "/__" );
    }
}

