package com.walnut.sparta.ucdn.console.infrastructure.vo;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.storage.bucket.entity.SiteNode;

public class SiteNodeVO implements Pinenut {
    protected long enumId;

    protected String nodeName;

    protected GUID nodeGuid;

    protected GUID siteGuid;

    protected int state;

    protected int isEnabled;

    protected GUID relatedService;

    protected String relatedServicePath;

    public SiteNodeVO(){}

    public SiteNodeVO(SiteNode siteNode){
        this.enumId = siteNode.getEnumId();
        this.nodeGuid = siteNode.getNodeGuid();
        this.nodeName = siteNode.getNodeName();
        this.relatedService = siteNode.getRelatedService();
        this.isEnabled = siteNode.getIsEnabled();
        this.siteGuid = siteNode.getSiteGuid();
        this.state = siteNode.getState();
    }


    public long getEnumId() {
        return this.enumId;
    }


    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }


    public String getNodeName() {
        return this.nodeName;
    }


    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }


    public GUID getNodeGuid() {
        return this.nodeGuid;
    }


    public void setNodeGuid(GUID nodeGuid) {
        this.nodeGuid = nodeGuid;
    }


    public GUID getSiteGuid() {
        return this.siteGuid;
    }


    public void setSiteGuid(GUID siteGuid) {
        this.siteGuid = siteGuid;
    }


    public int getState() {
        return this.state;
    }


    public void setState(int state) {
        this.state = state;
    }


    public int getIsEnabled() {
        return this.isEnabled;
    }


    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }


    public GUID getRelatedService() {
        return this.relatedService;
    }


    public void setRelatedService(GUID relatedService) {
        this.relatedService = relatedService;
    }

    public String getRelatedServicePath(){
        return this.relatedServicePath;
    }

    public void setRelatedServicePath(String relatedServicePath){
        this.relatedServicePath = relatedServicePath;
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
