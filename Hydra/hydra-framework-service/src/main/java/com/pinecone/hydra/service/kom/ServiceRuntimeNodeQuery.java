package com.pinecone.hydra.service.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class ServiceRuntimeNodeQuery implements Pinenut {

    public static final long DEFAULT_LIMIT = 20;

    public static final long MAX_LIMIT = 1000;

    protected long mOffset = 0;

    protected long mLimit = DEFAULT_LIMIT;

    protected String mszKeyword;

    protected GUID mGuid;

    protected GUID mServiceGuid;

    protected String mszNodeId;

    protected String mszStatus;

    public long getOffset() {
        return this.mOffset;
    }

    public void setOffset( long nOffset ) {
        this.mOffset = Math.max( 0, nOffset );
    }

    public long getLimit() {
        return this.mLimit;
    }

    public void setLimit( long nLimit ) {
        if ( nLimit <= 0 ) {
            this.mLimit = DEFAULT_LIMIT;
            return;
        }
        this.mLimit = Math.min( nLimit, MAX_LIMIT );
    }

    public String getKeyword() {
        return this.mszKeyword;
    }

    public void setKeyword( String keyword ) {
        this.mszKeyword = keyword;
    }

    public GUID getGuid() {
        return this.mGuid;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    public GUID getServiceGuid() {
        return this.mServiceGuid;
    }

    public void setServiceGuid( GUID serviceGuid ) {
        this.mServiceGuid = serviceGuid;
    }

    public String getNodeId() {
        return this.mszNodeId;
    }

    public void setNodeId( String nodeId ) {
        this.mszNodeId = nodeId;
    }

    public String getStatus() {
        return this.mszStatus;
    }

    public void setStatus( String status ) {
        this.mszStatus = status;
    }
}
