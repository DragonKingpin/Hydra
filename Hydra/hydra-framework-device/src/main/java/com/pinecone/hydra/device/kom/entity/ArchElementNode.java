package com.pinecone.hydra.device.kom.entity;

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
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.ArchDeviceFamilyMeta;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public abstract class ArchElementNode extends ArchDeviceFamilyMeta implements ElementNode {
    protected long                       enumId;

    protected GUID                       metaGuid;

    protected GUIDImperialTrieNode       distributedTreeNode;

    protected DeviceInstrument           deviceInstrument;

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

    public ArchElementNode( Map<String, Object > joEntity, DeviceInstrument deviceInstrument) {
        super( joEntity );
        this.apply(deviceInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public ArchElementNode( DeviceInstrument deviceInstrument) {
        this.apply(deviceInstrument);
    }

    public void apply( DeviceInstrument deviceInstrument) {
        this.deviceInstrument = deviceInstrument;
        if( this.getGuid() == null ) {
            GuidAllocator guidAllocator = this.deviceInstrument.getGuidAllocator();
            this.setGuid( guidAllocator.nextGUID() );
        }
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
        return this.deviceInstrument.getPath( this.getGuid() );
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
    public void setAlias( String alias ) {
        this.alias = alias;
    }

    @Override
    public void setExtraInformation( String extraInformation ) {
        this.extraInformation = extraInformation;
    }

    @Override
    public void setResourceType( String resourceType ) {
        this.resourceType = resourceType;
    }

    @Override
    public void setDeviceType( String deviceType ) {
        this.deviceType = deviceType;
    }

    @Override
    public void setVendor( String vendor ) {
        this.vendor = vendor;
    }

    @Override
    public void setModel( String model ) {
        this.model = model;
    }

    @Override
    public void setSerialNumber( String serialNumber ) {
        this.serialNumber = serialNumber;
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
    public void setStatus( String status ) {
        this.status = status;
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
            ElementNode elementNode = (ElementNode) this.deviceInstrument.get( guid );
            elementNodes.add( elementNode );
        }
        return elementNodes;
    }

    protected Collection<GUID > fetchChildrenGuids() {
        return this.deviceInstrument.fetchChildrenGuids( this.getGuid() );
    }

    protected void addChild( ElementNode child ) {
        GUID childId;
        boolean bContainsChild = this.containsChild( child.getName() );
        if( bContainsChild ) {
            return;
        }
        else {
            childId = this.deviceInstrument.put( child );
        }


        this.deviceInstrument.affirmOwnedNode( this.guid, childId );
    }

    protected boolean containsChild( String childName ) {
        return this.deviceInstrument.containsChild( this.guid, childName );
    }

    @Override
    public JSONObject toJSONObject() {
        return BeanColonist.DirectColonist.populate( this, UnbeanifiedKeys );
    }
}
