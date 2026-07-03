package com.pinecone.hydra.unit.imperium;

import com.pinecone.framework.util.id.GUID;

public final class PathCacheAtomicity {
    private PathCacheAtomicity() {
    }

    public static boolean sameGuid( GUID a, GUID b ) {
        if ( a == b ) {
            return true;
        }
        if ( a == null || b == null ) {
            return false;
        }
        return a.toString().equals( b.toString() );
    }

    public static boolean isDuplicateKey( Throwable throwable ) {
        Throwable current = throwable;
        while ( current != null ) {
            String className = current.getClass().getName();
            if ( className.endsWith( "DuplicateKeyException" ) ) {
                return true;
            }

            String message = current.getMessage();
            if ( message != null && message.contains( "Duplicate entry" ) ) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    public static IllegalStateException pathConflict(
            String cacheName, GUID existingGuid, GUID requestedGuid, String path
    ) {
        return new IllegalStateException(
                "Path cache conflict in " + cacheName
                        + ": path `" + path + "` already belongs to `" + existingGuid
                        + "`, requested `" + requestedGuid + "`."
        );
    }

    public static IllegalStateException insertExhausted( String cacheName, String path ) {
        return new IllegalStateException(
                "Failed to insert " + cacheName + " path cache after "
                        + ImperialTreeConstants.AtomicPathCacheInsertRetryLimit
                        + " atomic retries: " + path
        );
    }
}
