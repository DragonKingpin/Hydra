package com.pinecone.hydra.deploy.kom.entity;

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
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.ArchDeployFamilyMeta;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public abstract class ArchElementNode extends ArchDeployFamilyMeta implements ElementNode {
    protected long                       enumId;

    protected GUID                       metaGuid;

    protected GUIDImperialTrieNode       distributedTreeNode;

    protected DeployInstrument           deployInstrument;

    protected LocalDateTime              createTime;

    protected LocalDateTime              updateTime;

    public ArchElementNode() {
        super();

        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public ArchElementNode( Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public ArchElementNode( Map<String, Object > joEntity, DeployInstrument deployInstrument) {
        super( joEntity );
        this.apply(deployInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public ArchElementNode( DeployInstrument deployInstrument) {
        this.apply(deployInstrument);
    }

    public void apply( DeployInstrument deployInstrument) {
        this.deployInstrument = deployInstrument;
        GuidAllocator guidAllocator = this.deployInstrument.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID() );
        if ( this.createTime == null ) {
            this.createTime = LocalDateTime.now();
            this.updateTime = LocalDateTime.now();
        }
    }

    @Override
    public ArchElementNode apply( Map<String, Object > joEntity ) {
        super.apply( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );

        return this;
    }

    @Override
    public String getKomPath() {
        return this.deployInstrument.getPath( this.getGuid() );
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
    public void setExtraInformation( String extraInformation ) {
        this.extraInformation = extraInformation;
    }
    @Override
    public void setIpAddress( String ipAddress ) {
        this.ipAddress = ipAddress;
    }
    @Override
    public String getIpAddress() {
        return this.ipAddress;
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
            ElementNode elementNode = (ElementNode) this.deployInstrument.get( guid );
            elementNodes.add( elementNode );
        }
        return elementNodes;
    }

    protected Collection<GUID > fetchChildrenGuids() {
        return this.deployInstrument.fetchChildrenGuids( this.getGuid() );
    }

    protected void addChild( ElementNode child ) {
        GUID childId;
        boolean bContainsChild = this.containsChild( child.getName() );
        if( bContainsChild ) {
            return;
        }
        else {
            childId = this.deployInstrument.put( child );
        }


        this.deployInstrument.affirmOwnedNode( this.guid, childId );
    }

    protected boolean containsChild( String childName ) {
        return this.deployInstrument.containsChild( this.guid, childName );
    }

    @Override
    public JSONObject toJSONObject() {
        return BeanColonist.DirectColonist.populate( this, UnbeanifiedKeys );
    }
}