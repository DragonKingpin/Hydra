package com.walnut.redstone.ether.s3.http;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectRange;
import com.walnut.redstone.ether.s3.error.S3ErrorCode;
import com.walnut.redstone.ether.s3.error.S3ProtocolException;

public class S3RangeParser implements Pinenut {
    public ObjectRange parse( String rangeHeader, long size ) {
        ObjectRange ret = new ObjectRange();
        if ( rangeHeader == null || rangeHeader.trim().isEmpty() ) {
            ret.setStart( 0L );
            ret.setEnd( Math.max( size - 1L, 0L ) );
            ret.setLength( size );
            ret.setPartial( false );
            return ret;
        }
        if ( size <= 0L ) {
            throw new S3ProtocolException( S3ErrorCode.INVALID_RANGE, "Range is not satisfiable for empty object." );
        }

        String value = rangeHeader.trim();
        if ( !value.startsWith( "bytes=" ) || value.contains( "," ) ) {
            throw new S3ProtocolException( S3ErrorCode.INVALID_RANGE, "Only single bytes range is supported." );
        }
        String spec = value.substring( "bytes=".length() ).trim();
        int split = spec.indexOf( "-" );
        if ( split < 0 ) {
            throw new S3ProtocolException( S3ErrorCode.INVALID_RANGE, "Invalid range: " + rangeHeader );
        }

        String startPart = spec.substring( 0, split ).trim();
        String endPart = spec.substring( split + 1 ).trim();
        long start;
        long end;
        try {
            if ( startPart.isEmpty() ) {
                long suffix = Long.parseLong( endPart );
                if ( suffix <= 0L ) {
                    throw new S3ProtocolException( S3ErrorCode.INVALID_RANGE, "Invalid suffix range: " + rangeHeader );
                }
                start = Math.max( size - suffix, 0L );
                end = size - 1L;
            }
            else {
                start = Long.parseLong( startPart );
                end = endPart.isEmpty() ? size - 1L : Long.parseLong( endPart );
            }
        }
        catch ( NumberFormatException ex ) {
            throw new S3ProtocolException( S3ErrorCode.INVALID_RANGE, "Invalid range: " + rangeHeader, null, ex );
        }

        if ( start < 0L || end < start || start >= size ) {
            throw new S3ProtocolException( S3ErrorCode.INVALID_RANGE, "Range is not satisfiable: " + rangeHeader );
        }
        end = Math.min( end, size - 1L );
        ret.setStart( start );
        ret.setEnd( end );
        ret.setLength( end - start + 1L );
        ret.setPartial( true );
        return ret;
    }
}
