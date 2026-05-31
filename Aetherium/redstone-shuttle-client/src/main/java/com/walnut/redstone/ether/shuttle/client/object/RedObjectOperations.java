package com.walnut.redstone.ether.shuttle.client.object;

import java.io.InputStream;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectListResult;
import com.walnut.redstone.ether.object.ObjectMetadata;

public interface RedObjectOperations extends Pinenut {
    RedObjectStream get( String szUri );

    RedObjectStream get( String szUri, RedObjectGetOptions options );

    RedObjectStream get( RedObjectRef ref, RedObjectGetOptions options );

    ObjectMetadata stat( String szUri );

    ObjectMetadata stat( RedObjectRef ref );

    RedObjectPutResult put( String szUri, InputStream inputStream, long nSize, RedObjectPutOptions options );

    RedObjectPutResult put( RedObjectRef ref, InputStream inputStream, long nSize, RedObjectPutOptions options );

    void delete( String szUri );

    void delete( RedObjectRef ref );

    ObjectListResult list( String szUri, RedObjectListOptions options );

    ObjectListResult list( RedBucketRef bucketRef, RedObjectListOptions options );
}
