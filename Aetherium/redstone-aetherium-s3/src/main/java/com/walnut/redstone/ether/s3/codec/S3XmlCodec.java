package com.walnut.redstone.ether.s3.codec;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.s3.model.S3BucketInfo;
import com.walnut.redstone.ether.s3.model.S3ListBucketResult;
import com.walnut.redstone.ether.s3.model.S3ObjectInfo;
import com.walnut.redstone.ether.s3.error.S3ErrorCode;
import com.walnut.redstone.ether.s3.error.S3ProtocolException;

public class S3XmlCodec implements Pinenut {
    public String listBuckets( List<S3BucketInfo> buckets ) {
        StringBuilder builder = this.begin( "ListAllMyBucketsResult" );
        builder.append( "<Buckets>" );
        if ( buckets != null ) {
            for ( S3BucketInfo bucket : buckets ) {
                builder.append( "<Bucket>" )
                        .append( this.element( "Name", bucket.getBucketName() ) )
                        .append( this.element( "CreationDate", this.time( bucket.getCreationDate() ) ) )
                        .append( "</Bucket>" );
            }
        }
        builder.append( "</Buckets>" );
        return this.end( builder, "ListAllMyBucketsResult" );
    }

    public String listObjects( S3ListBucketResult result ) {
        StringBuilder builder = this.begin( "ListBucketResult" );
        builder.append( this.element( "Name", result.getBucketName() ) )
                .append( this.element( "Prefix", result.getPrefix() ) )
                .append( this.element( "Marker", result.getMarker() ) )
                .append( this.element( "Delimiter", result.getDelimiter() ) )
                .append( this.element( "EncodingType", result.getEncodingType() ) )
                .append( this.element( "MaxKeys", String.valueOf( result.getMaxKeys() ) ) )
                .append( this.element( "IsTruncated", String.valueOf( result.isTruncated() ) ) );
        for ( S3ObjectInfo object : result.getObjects() ) {
            builder.append( "<Contents>" )
                    .append( this.element( "Key", object.getKey() ) )
                    .append( this.element( "LastModified", this.time( object.getLastModified() ) ) )
                    .append( this.element( "ETag", object.getEtag() ) )
                    .append( this.element( "Size", String.valueOf( object.getSize() == null ? 0L : object.getSize() ) ) )
                    .append( "</Contents>" );
        }
        return this.end( builder, "ListBucketResult" );
    }

    public String error( S3ProtocolException exception, String requestPath ) {
        StringBuilder builder = this.begin( "Error" );
        builder.append( this.element( "Code", exception.getErrorCode().getCode() ) )
                .append( this.element( "Message", exception.getMessage() ) )
                .append( this.element( "Resource", exception.getResource() == null ? requestPath : exception.getResource() ) );
        return this.end( builder, "Error" );
    }

    public String error( S3ErrorCode code, String message, String requestPath ) {
        return this.error( new S3ProtocolException( code, message, requestPath ), requestPath );
    }

    protected StringBuilder begin( String root ) {
        return new StringBuilder()
                .append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" )
                .append( "<" ).append( root ).append( ">" );
    }

    protected String end( StringBuilder builder, String root ) {
        return builder.append( "</" ).append( root ).append( ">" ).toString();
    }

    protected String element( String name, String value ) {
        return "<" + name + ">" + this.escape( value ) + "</" + name + ">";
    }

    protected String time( LocalDateTime value ) {
        if ( value == null ) {
            return "";
        }
        return DateTimeFormatter.ISO_INSTANT.format( value.toInstant( ZoneOffset.UTC ) );
    }

    protected String escape( String value ) {
        String ret = value == null ? "" : value;
        return ret.replace( "&", "&amp;" )
                .replace( "<", "&lt;" )
                .replace( ">", "&gt;" )
                .replace( "\"", "&quot;" )
                .replace( "'", "&apos;" );
    }
}



