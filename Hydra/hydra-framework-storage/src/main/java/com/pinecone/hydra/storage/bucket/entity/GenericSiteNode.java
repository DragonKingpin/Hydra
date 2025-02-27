package com.pinecone.hydra.storage.bucket.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;

public class GenericSiteNode implements SiteNode{
    protected long enumId;

    protected String nodeName;

    protected GUID nodeGuid;

    protected GUID siteGuid;

    protected int state;

    protected int isEnabled;

    protected GUID relatedService;

    @Override
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }

    @Override
    public String getNodeName() {
        return this.nodeName;
    }

    @Override
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public GUID getNodeGuid() {
        return this.nodeGuid;
    }

    @Override
    public void setNodeGuid(GUID nodeGuid) {
        this.nodeGuid = nodeGuid;
    }

    @Override
    public GUID getSiteGuid() {
        return this.siteGuid;
    }

    @Override
    public void setSiteGuid(GUID siteGuid) {
        this.siteGuid = siteGuid;
    }

    @Override
    public int getState() {
        return this.state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int getIsEnabled() {
        return this.isEnabled;
    }

    @Override
    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public GUID getRelatedService() {
        return this.relatedService;
    }

    @Override
    public void setRelatedService(GUID relatedService) {
        this.relatedService = relatedService;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
