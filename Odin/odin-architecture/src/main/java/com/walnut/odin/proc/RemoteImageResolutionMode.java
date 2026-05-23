package com.walnut.odin.proc;

public enum RemoteImageResolutionMode {
    REQUIRE_SERVER_IMAGE,
    REMOTE_CLIENT_IMAGE;

    public static RemoteImageResolutionMode parse( String szMode ) {
        if ( szMode == null || szMode.isEmpty() ) {
            return REQUIRE_SERVER_IMAGE;
        }

        try {
            return RemoteImageResolutionMode.valueOf( szMode );
        }
        catch ( IllegalArgumentException e ) {
            return REQUIRE_SERVER_IMAGE;
        }
    }
}
