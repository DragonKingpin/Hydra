package com.walnut.redstone.ether.s3.http;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectRange;
import com.walnut.redstone.ether.s3.model.S3ObjectInfo;

public class S3ObjectReadResult implements Pinenut {
    protected S3ObjectInfo objectInfo;
    protected ObjectRange range;
    protected S3HttpResponse response;

    public S3ObjectInfo getObjectInfo() {
        return this.objectInfo;
    }

    public void setObjectInfo( S3ObjectInfo objectInfo ) {
        this.objectInfo = objectInfo;
    }

    public ObjectRange getRange() {
        return this.range;
    }

    public void setRange( ObjectRange range ) {
        this.range = range;
    }

    public S3HttpResponse getResponse() {
        return this.response;
    }

    public void setResponse( S3HttpResponse response ) {
        this.response = response;
    }
}
