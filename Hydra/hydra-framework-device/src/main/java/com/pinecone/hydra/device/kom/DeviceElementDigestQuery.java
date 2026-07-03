package com.pinecone.hydra.device.kom;

import com.pinecone.framework.system.prototype.Pinenut;

public class DeviceElementDigestQuery implements Pinenut {

    public static final long DEFAULT_LIMIT = 20;

    public static final long MAX_LIMIT = 1000;

    protected long offset = 0;

    protected long limit = DEFAULT_LIMIT;

    protected String path;

    protected String category;

    protected String deploymentProfile;

    protected String topologyRole;

    protected String lifecycleStatus;

    protected String region;

    protected String tags;

    protected String keyword;

    public long getOffset() {
        return this.offset;
    }

    public void setOffset( long offset ) {
        this.offset = Math.max( 0, offset );
    }

    public long getLimit() {
        return this.limit;
    }

    public void setLimit( long limit ) {
        if ( limit <= 0 ) {
            this.limit = DEFAULT_LIMIT;
            return;
        }
        this.limit = Math.min( limit, MAX_LIMIT );
    }

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory( String category ) {
        this.category = category;
    }

    public String getDeploymentProfile() {
        return this.deploymentProfile;
    }

    public void setDeploymentProfile( String deploymentProfile ) {
        this.deploymentProfile = deploymentProfile;
    }

    public String getTopologyRole() {
        return this.topologyRole;
    }

    public void setTopologyRole( String topologyRole ) {
        this.topologyRole = topologyRole;
    }

    public String getLifecycleStatus() {
        return this.lifecycleStatus;
    }

    public void setLifecycleStatus( String lifecycleStatus ) {
        this.lifecycleStatus = lifecycleStatus;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion( String region ) {
        this.region = region;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags( String tags ) {
        this.tags = tags;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword( String keyword ) {
        this.keyword = keyword;
    }
}
