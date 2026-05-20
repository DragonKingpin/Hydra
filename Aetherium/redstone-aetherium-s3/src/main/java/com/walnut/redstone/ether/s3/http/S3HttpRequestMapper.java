package com.walnut.redstone.ether.s3.http;

import java.io.InputStream;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectContent;
import com.walnut.redstone.ether.s3.model.S3ListObjectsV1Request;
import com.walnut.redstone.ether.s3.path.S3PathStyleResolver;

public class S3HttpRequestMapper implements Pinenut {
    protected final S3PathStyleResolver pathResolver = new S3PathStyleResolver();

    public S3ListObjectsV1Request listObjectsRequest(
            String prefix,
            String marker,
            String delimiter,
            String encodingType,
            Integer maxKeys
    ) {
        S3ListObjectsV1Request ret = new S3ListObjectsV1Request();
        ret.setPrefix( prefix );
        ret.setMarker( marker );
        ret.setDelimiter( delimiter );
        ret.setEncodingType( encodingType );
        ret.setMaxKeys( maxKeys );
        return ret;
    }

    public String objectKey( String pathWithinMapping, String bucketName ) {
        return this.pathResolver.keyFromRequestPath( pathWithinMapping, bucketName );
    }

    public ObjectContent objectContent( InputStream inputStream, long size, String contentType ) {
        ObjectContent ret = new ObjectContent();
        ret.setInputStream( inputStream );
        ret.setSize( size );
        ret.setContentType( contentType );
        return ret;
    }
}
