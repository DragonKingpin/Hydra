package com.pinecone.hydra.business.entity.tag;

import com.pinecone.framework.util.id.GUID;

public class NodeTag {
    protected GUID nodeGuid;
    protected String tag;

    public GUID getNodeGuid() { return this.nodeGuid; }
    public void setNodeGuid( GUID nodeGuid ) { this.nodeGuid = nodeGuid; }
    public String getTag() { return this.tag; }
    public void setTag( String tag ) { this.tag = tag; }
}
