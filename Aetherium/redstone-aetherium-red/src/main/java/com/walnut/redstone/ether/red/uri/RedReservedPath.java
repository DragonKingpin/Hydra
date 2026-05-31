package com.walnut.redstone.ether.red.uri;

import com.walnut.redstone.ether.red.ReservedPaths;

public final class RedReservedPath {
    public static final String Red = ReservedPaths.Red;

    private RedReservedPath() {
    }

    public static boolean isReserved( String path ) {
        return ReservedPaths.isReservedPath( path );
    }
}
