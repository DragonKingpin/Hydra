package com.walnut.redstone.ether.s3.error;

import com.pinecone.framework.system.prototype.Pinenut;

public class S3ErrorMapper implements Pinenut {
    public S3ProtocolException internal( Throwable throwable ) {
        return new S3ProtocolException( S3ErrorCode.INTERNAL_ERROR, throwable == null ? null : throwable.getMessage(), null, throwable );
    }
}

