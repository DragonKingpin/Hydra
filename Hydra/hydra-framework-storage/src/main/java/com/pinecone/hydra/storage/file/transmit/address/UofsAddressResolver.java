package com.pinecone.hydra.storage.file.transmit.address;

import com.pinecone.framework.system.prototype.Pinenut;

public class UofsAddressResolver implements Pinenut {
    public UofsAddress resolve( String path, UofsResolveContext context ) {
        if ( path == null ) {
            throw new IllegalArgumentException( "UOFS path is null" );
        }
        String normalized = path.trim().replace( '\\', '/' );
        String mountPath = context.getHydraMountPath();
        if ( mountPath != null && !mountPath.trim().isEmpty() ) {
            String normalizedMount = this.trimSlashes( mountPath.trim().replace( '\\', '/' ) );
            String candidate = this.trimSlashes( normalized );
            if ( candidate.equals( normalizedMount ) ) {
                normalized = "";
            }
            else if ( candidate.startsWith( normalizedMount + "/" ) ) {
                normalized = candidate.substring( normalizedMount.length() + 1 );
            }
        }
        normalized = this.trimSlashes( normalized );

        String ownerName = context.getDefaultOwnerName();
        String bucketName = context.getDefaultBucketName();
        String key = normalized;
        int firstSlash = normalized.indexOf( '/' );
        String firstPart = firstSlash >= 0 ? normalized.substring( 0, firstSlash ) : normalized;
        String rest = firstSlash >= 0 ? normalized.substring( firstSlash + 1 ) : "";

        if ( firstPart.contains( "@" ) ) {
            String[] pair = firstPart.split( "@", 2 );
            if ( pair.length == 2 && !pair[0].isEmpty() && !pair[1].isEmpty() ) {
                ownerName = pair[0];
                bucketName = pair[1];
                key = rest;
            }
        }
        else if ( firstPart.equals( context.getDefaultBucketName() ) ) {
            key = rest;
        }

        return new UofsAddress( ownerName, bucketName, key, mountPath );
    }

    protected String trimSlashes( String value ) {
        String ret = value;
        while ( ret.startsWith( "/" ) ) {
            ret = ret.substring( 1 );
        }
        while ( ret.endsWith( "/" ) ) {
            ret = ret.substring( 0, ret.length() - 1 );
        }
        return ret;
    }
}
