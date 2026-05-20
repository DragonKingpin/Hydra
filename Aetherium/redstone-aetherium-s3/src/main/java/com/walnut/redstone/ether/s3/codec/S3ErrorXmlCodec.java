package com.walnut.redstone.ether.s3.codec;

import com.walnut.redstone.ether.s3.error.S3ErrorCode;
import com.walnut.redstone.ether.s3.error.S3ProtocolException;

public class S3ErrorXmlCodec extends S3XmlCodec {
    public String error( S3ErrorCode code, String message, String requestPath ) {
        return super.error( new S3ProtocolException( code, message, requestPath ), requestPath );
    }
}

