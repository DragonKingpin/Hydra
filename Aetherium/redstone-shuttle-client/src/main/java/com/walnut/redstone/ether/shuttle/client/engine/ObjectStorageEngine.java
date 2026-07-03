package com.walnut.redstone.ether.shuttle.client.engine;

import java.io.InputStream;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectListResult;
import com.walnut.redstone.ether.object.ObjectMetadata;
import com.walnut.redstone.ether.shuttle.client.object.RedBucketRef;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectGetOptions;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectListOptions;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectPutOptions;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectPutResult;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectRef;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectStream;

public interface ObjectStorageEngine extends Pinenut {
    RedObjectStream getObject( RedObjectRef ref, RedObjectGetOptions options );

    ObjectMetadata statObject( RedObjectRef ref );

    RedObjectPutResult putObject( RedObjectRef ref, InputStream inputStream, long nSize, RedObjectPutOptions options );

    void deleteObject( RedObjectRef ref );

    ObjectListResult listObjects( RedBucketRef bucketRef, RedObjectListOptions options );
}
