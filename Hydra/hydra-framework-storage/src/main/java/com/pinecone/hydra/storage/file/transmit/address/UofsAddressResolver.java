package com.pinecone.hydra.storage.file.transmit.address;

import com.pinecone.framework.system.prototype.Pinenut;

public class UofsAddressResolver implements Pinenut {
    public UofsAddress resolve( String path, UofsResolveContext context ) {
        if ( path == null ) {
            throw new IllegalArgumentException( "UOFS path is null" );
        }
        String separator = context.getPathNameSeparator();
        String normalized = path.trim();
        String mountPath = context.getHydraMountPath();
        if ( mountPath != null && !mountPath.trim().isEmpty() ) {
            String normalizedMount = this.trimSlashes( mountPath.trim(), separator );
            String candidate = this.trimSlashes( normalized, separator );
            if ( candidate.equals( normalizedMount ) ) {
                normalized = "";
            }
            else if ( candidate.startsWith( normalizedMount + separator ) ) {
                normalized = candidate.substring( normalizedMount.length() + separator.length() );
            }
        }
        normalized = this.trimSlashes( normalized, separator );

        String userIdentifier = context.getDefaultUserIdentifier();
        String bucketName = context.getDefaultBucketName();
        String key = normalized;
        int firstSlash = normalized.indexOf( separator );
        String firstPart = firstSlash >= 0 ? normalized.substring( 0, firstSlash ) : normalized;
        String rest = firstSlash >= 0 ? normalized.substring( firstSlash + separator.length() ) : "";

        if ( firstPart.contains( "@" ) ) {
            String[] pair = firstPart.split( "@", 2 );
            if ( pair.length == 2 && !pair[0].isEmpty() && !pair[1].isEmpty() ) {
                userIdentifier = pair[0];
                bucketName = pair[1];
                key = rest;
            }
        }
        else if ( firstPart.equals( context.getDefaultBucketName() ) ) {
            key = rest;
        }

        return new UofsAddress( userIdentifier, bucketName, key, mountPath );
    }

    protected String trimSlashes( String value, String separator ) {
        String ret = value;
        while ( ret.startsWith( separator ) ) {
            ret = ret.substring( separator.length() );
        }
        while ( ret.endsWith( separator ) ) {
            ret = ret.substring( 0, ret.length() - separator.length() );
        }
        return ret;
    }
}
