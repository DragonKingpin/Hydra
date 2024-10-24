package com.pinecone.hydra.service.kom.entity;

import java.time.LocalDateTime;
import java.util.Set;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.ulf.util.id.GuidAllocator;

public class GenericApplicationElement extends ArchElementNode implements ApplicationElement {
    protected GUIDDistributedTrieNode distributedTreeNode;

    protected GUID metaGuid;
    protected String path;
    protected String type;
    protected String alias;
    protected String resourceType;
    protected String deploymentMethod;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    private ServicesInstrument servicesInstrument;
    private ApplicationNodeManipulator applicationNodeManipulator;


    public GenericApplicationElement() {
        super();
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public GenericApplicationElement(ServicesInstrument servicesInstrument) {
        this.servicesInstrument = servicesInstrument;
        GuidAllocator guidAllocator = this.servicesInstrument.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID72() );
    }

    public GenericApplicationElement(ServicesInstrument servicesInstrument, ApplicationNodeManipulator applicationNodeManipulator ) {
        this(servicesInstrument);
        this.applicationNodeManipulator = applicationNodeManipulator;
    }

    @Override
    public GUIDDistributedTrieNode getDistributedTreeNode() {
        return distributedTreeNode;
    }

    @Override
    public void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }


    /**
     *  Overridden to keep keys in prior json-decode.
     */
    @Override
    public GUID getGuid() {
        return super.getGuid();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public GUID getMetaGuid() {
        return this.metaGuid;
    }

    @Override
    public void setMetaGuid( GUID metaGuid ) {
        this.metaGuid = metaGuid;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getResourceType() {
        return resourceType;
    }

    @Override
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public String getDeploymentMethod() {
        return deploymentMethod;
    }

    @Override
    public void setDeploymentMethod(String deploymentMethod) {
        this.deploymentMethod = deploymentMethod;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this, Set.of(
                "distributedTreeNode"
        ) );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}