package com.walnut.sparta.ucdn.console.infrastructure.dto;

import com.pinecone.framework.util.id.GUID;

public class SiteNodeDTO {
    protected long enumId;

    protected String nodeName;

    protected String nodeGuid;

    protected String siteGuid;

    protected int state;

    protected int isEnabled;

    protected String relatedService;

    public long getEnumId() {
        return enumId;
    }

    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeGuid() {
        return nodeGuid;
    }

    public void setNodeGuid(String nodeGuid) {
        this.nodeGuid = nodeGuid;
    }

    public String getSiteGuid() {
        return siteGuid;
    }

    public void setSiteGuid(String siteGuid) {
        this.siteGuid = siteGuid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getRelatedService() {
        return relatedService;
    }

    public void setRelatedService(String relatedService) {
        this.relatedService = relatedService;
    }
}
