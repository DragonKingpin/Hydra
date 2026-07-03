package com.walnut.redstone.ether.s3.path;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.s3.error.S3ErrorCode;
import com.walnut.redstone.ether.s3.error.S3ProtocolException;

public class S3PathStyleResolver implements Pinenut {
    public String normalizeBucketName( String bucketName ) {
        String ret = this.decode( bucketName ).trim();
        if ( ret.isEmpty() || ret.contains( "/" ) ) {
            throw new S3ProtocolException( S3ErrorCode.INVALID_ARGUMENT, "Bucket name is invalid." );
        }
        return ret;
    }

    public String normalizeKey( String key ) {
        String ret = this.decode( key == null ? "" : key );
        ret = ret.replace( "\\", "/" );
        while ( ret.startsWith( "/" ) ) {
            ret = ret.substring( 1 );
        }
        if ( ret.isEmpty() ) {
            throw new S3ProtocolException( S3ErrorCode.INVALID_ARGUMENT, "Object key should not be blank." );
        }
        return ret;
    }

    public String normalizePrefix( String prefix ) {
        String ret = this.decode( prefix == null ? "" : prefix );
        ret = ret.replace( "\\", "/" );
        while ( ret.startsWith( "/" ) ) {
            ret = ret.substring( 1 );
        }
        return ret;
    }

    public String displayPath( String key ) {
        String normalized = this.normalizeKey( key );
        return "/" + normalized;
    }

    public String keyFromRequestPath( String pathWithinMapping, String bucketName ) {
        String value = pathWithinMapping == null ? "" : pathWithinMapping.trim();
        while ( value.startsWith( "/" ) ) {
            value = value.substring( 1 );
        }
        String prefix = bucketName + "/";
        if ( value.startsWith( prefix ) ) {
            value = value.substring( prefix.length() );
        }
        return this.normalizeKey( value );
    }

    protected String decode( String value ) {
        try {
            return URLDecoder.decode( value == null ? "" : value, StandardCharsets.UTF_8.name() );
        }
        catch ( UnsupportedEncodingException ex ) {
            throw new IllegalStateException( ex );
        }
    }
}




