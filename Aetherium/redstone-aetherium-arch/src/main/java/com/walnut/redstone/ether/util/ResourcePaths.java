package com.walnut.redstone.ether.util;

public final class ResourcePaths {
    private ResourcePaths() {
    }

    public static String normalize( String path ) {
        String ret = path == null ? "" : path.replace( "\\", "/" ).trim();
        while ( ret.startsWith( "/" ) ) {
            ret = ret.substring( 1 );
        }
        return ret;
    }
}

