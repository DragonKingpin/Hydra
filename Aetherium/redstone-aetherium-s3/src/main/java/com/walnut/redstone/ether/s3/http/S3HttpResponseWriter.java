package com.walnut.redstone.ether.s3.http;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectRange;
import com.walnut.redstone.ether.s3.model.S3ObjectInfo;

public class S3HttpResponseWriter implements Pinenut {
    public S3HttpResponse empty( int statusCode ) {
        S3HttpResponse ret = new S3HttpResponse();
        ret.setStatusCode( statusCode );
        return ret;
    }

    public S3HttpResponse xml( String body ) {
        S3HttpResponse ret = new S3HttpResponse();
        ret.setHeader( S3HeaderNames.ContentType, "application/xml" );
        ret.setBody( body );
        return ret;
    }

    public S3HttpResponse objectRead( S3ObjectInfo info, ObjectRange range ) {
        S3HttpResponse ret = new S3HttpResponse();
        ret.setHeader( S3HeaderNames.AcceptRanges, "bytes" );
        ret.setHeader( S3HeaderNames.ETag, info.getEtag() );
        ret.setHeader( S3HeaderNames.ContentType, info.getContentType() );
        ret.setHeader( S3HeaderNames.ContentLength, String.valueOf( range.getLength() ) );
        if ( info.getLastModified() != null ) {
            ret.setHeader(
                    S3HeaderNames.LastModified,
                    DateTimeFormatter.RFC_1123_DATE_TIME.format( info.getLastModified().atOffset( ZoneOffset.UTC ) )
            );
        }
        if ( range.isPartial() ) {
            ret.setStatusCode( 206 );
            ret.setHeader( S3HeaderNames.ContentRange, "bytes " + range.getStart() + "-" + range.getEnd() + "/" + info.getSize() );
        }
        return ret;
    }
}
