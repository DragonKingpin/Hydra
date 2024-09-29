package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;

public abstract class ArchConfigNode implements ConfigNode {
    protected int                     enumId;
    protected GUID                    guid;
    protected GUID                    nsGuid;
    protected GUID                    parentGuid;
    protected LocalDateTime           createTime;
    protected LocalDateTime           updateTime;
    protected String                  name;

    protected GenericConfigNodeMeta   configNodeMeta;
    protected GenericNodeAttribute    nodeCommonData;
    protected DistributedRegistry     registry;

    protected ArchConfigNode() {

    }

    public ArchConfigNode( DistributedRegistry registry ) {
        this.registry = registry;
    }

    public ArchConfigNode(
            DistributedRegistry registry,
            int enumId, GUID guid, GUID nsGuid, GUID parentGuid, LocalDateTime createTime,
            LocalDateTime updateTime, String name,
            GenericConfigNodeMeta configNodeMeta, GenericNodeAttribute nodeCommonData
    ) {
        this.registry         = registry;
        this.enumId           = enumId;
        this.guid             = guid;
        this.nsGuid           = nsGuid;
        this.parentGuid       = parentGuid;
        this.createTime       = createTime;
        this.updateTime       = updateTime;
        this.name             = name;
        this.configNodeMeta   = configNodeMeta;
        this.nodeCommonData   = nodeCommonData;
    }

    public void apply( DistributedRegistry registry ) {
        this.registry = registry;
    }

    @Override
    public int getEnumId() {
        return this.enumId;
    }


    @Override
    public void setEnumId( int enumId ) {
        this.enumId = enumId;
    }


    @Override
    public GUID getGuid() {
        return this.guid;
    }


    @Override
    public void setGuid( GUID guid ) {
        this.guid = guid;
    }


    @Override
    public GUID getNsGuid() {
        return this.nsGuid;
    }


    @Override
    public void setNsGuid( GUID nsGuid ) {
        this.nsGuid = nsGuid;
    }


    @Override
    public GUID getParentGuid() {
        return this.parentGuid;
    }


    @Override
    public void setParentGuid( GUID parentGuid ) {
        this.parentGuid = parentGuid;
    }


    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }


    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }


    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }


    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }


    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public GenericConfigNodeMeta getConfigNodeMeta() {
        return this.configNodeMeta;
    }


    @Override
    public void setConfigNodeMeta( GenericConfigNodeMeta configNodeMeta ) {
        this.configNodeMeta = configNodeMeta;
    }


    @Override
    public GenericNodeAttribute getNodeCommonData() {
        return this.nodeCommonData;
    }


    @Override
    public void setNodeCommonData( GenericNodeAttribute nodeCommonData ) {
        this.nodeCommonData = nodeCommonData;
    }

    @Override
    public DistributedRegistry getRegistry() {
        return this.registry;
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
