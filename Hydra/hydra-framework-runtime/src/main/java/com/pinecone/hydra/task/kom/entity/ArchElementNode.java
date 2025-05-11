package com.pinecone.hydra.task.kom.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.ArchTaskFamilyMeta;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public abstract class ArchElementNode extends ArchTaskFamilyMeta implements ElementNode {
    protected long                       enumId;

    protected GUID                       metaGuid;

    protected GUIDImperialTrieNode       distributedTreeNode;

    protected TaskInstrument             taskInstrument;

    protected LocalDateTime              createTime;

    protected LocalDateTime              updateTime;

    public ArchElementNode() {
        super();

        this.createTime = LocalDateTime.now();
        this.createTime = LocalDateTime.now();
    }

    public ArchElementNode( Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        this.createTime = LocalDateTime.now();
        this.createTime = LocalDateTime.now();
    }

    public ArchElementNode( Map<String, Object > joEntity, TaskInstrument taskInstrument) {
        super( joEntity );
        this.apply(taskInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public ArchElementNode( TaskInstrument taskInstrument ) {
        this.apply(taskInstrument);
    }

    public void apply( TaskInstrument taskInstrument ) {
        this.taskInstrument = taskInstrument;
        GuidAllocator guidAllocator = this.taskInstrument.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID() );
        this.createTime = LocalDateTime.now();
        this.createTime = LocalDateTime.now();
    }

    @Override
    public ArchElementNode apply( Map<String, Object > joEntity ) {
        super.apply( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );

        return this;
    }

    @Override
    public String getKomPath() {
        return this.taskInstrument.getPath( this.getGuid() );
    }

    @Override
    public String getSystemKernelObjectPath() {
        return this.taskInstrument.querySystemKernelObjectPath( this.getGuid() );
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
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public void setEnumId( long enumId ) {
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
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public void setScenario( String scenario ) {
        this.scenario = scenario;
    }

    @Override
    public void setMarshallingArchitecture( String marshallingArchitecture ) {
        this.marshallingArchitecture = marshallingArchitecture;
    }

    @Override
    public void setExtraInformation( String extraInformation ) {
        this.extraInformation = extraInformation;
    }

    @Override
    public void setDescription( String description ) {
        this.description = description;
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
    public GUIDImperialTrieNode getDistributedTreeNode() {
        return this.distributedTreeNode;
    }

    @Override
    public void setDistributedTreeNode( GUIDImperialTrieNode distributedTreeNode ) {
        this.distributedTreeNode = distributedTreeNode;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }


    protected Collection<ElementNode > fetchChildren() {
        Collection<GUID > guids = this.fetchChildrenGuids();
        List<ElementNode > elementNodes = new ArrayList<>();
        for( GUID guid : guids ){
            ElementNode elementNode = (ElementNode) this.taskInstrument.get( guid );
            elementNodes.add( elementNode );
        }
        return elementNodes;
    }

    protected Collection<GUID > fetchChildrenGuids() {
        return this.taskInstrument.fetchChildrenGuids( this.getGuid() );
    }

    protected void addChild( ElementNode child ) {
        GUID childId;
        boolean bContainsChild = this.containsChild( child.getName() );
        if( bContainsChild ) {
            return;
        }
        else {
            childId = this.taskInstrument.put( child );
        }


        this.taskInstrument.affirmOwnedNode( this.guid, childId );
    }

    protected boolean containsChild( String childName ) {
        return this.taskInstrument.containsChild( this.guid, childName );
    }

    @Override
    public JSONObject toJSONObject() {
        return BeanColonist.DirectColonist.populate( this, UnbeanifiedKeys );
    }
}