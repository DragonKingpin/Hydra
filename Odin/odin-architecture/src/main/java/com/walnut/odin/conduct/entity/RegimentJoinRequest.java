package com.walnut.odin.conduct.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class RegimentJoinRequest implements Pinenut {

    protected String mszNodeName;
    protected Long   mnClientId;
    protected Map<String, String> mMetadata;

    public RegimentJoinRequest() {
        this.mMetadata = new LinkedHashMap<>();
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

    public Map<String, String> getMetadata() {
        return this.mMetadata;
    }

    public void setMetadata( Map<String, String> metadata ) {
        this.mMetadata = metadata == null ? new LinkedHashMap<>() : new LinkedHashMap<>( metadata );
    }

}
