package com.walnut.odin.conduct.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class RegimentJoinRequest implements Pinenut {

    protected String mszNodeName;
    protected Long   mnClientId;

    public RegimentJoinRequest() {
    }

    public String getNodeName() {
        return this.mszNodeName;
    }

    public void setNodeName( String szNodeName ) {
        this.mszNodeName = szNodeName;
    }

    public Long getClientId() {
        return this.mnClientId;
    }

    public void setClientId( Long nClientId ) {
        this.mnClientId = nClientId;
    }


}
