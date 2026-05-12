package com.pinecone.hydra.device.kom.entity;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.source.DeviceNamespaceManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public class GenericNamespace extends ArchElementNode implements Namespace {
    protected GUID                        metaGuid;

    protected GUIDImperialTrieNode        distributedTreeNode;

    protected DeviceNamespaceManipulator  namespaceManipulator;


    public GenericNamespace() {
        super();
    }

    public GenericNamespace( Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericNamespace( Map<String, Object > joEntity, DeviceInstrument deviceInstrument) {
        super( joEntity, deviceInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericNamespace( DeviceInstrument deviceInstrument) {
        super(deviceInstrument);
    }

    public GenericNamespace(DeviceInstrument deviceInstrument, DeviceNamespaceManipulator namespaceManipulator ) {
        this(deviceInstrument);
        this.namespaceManipulator = namespaceManipulator;
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
    public GUID getMetaGuid() {
        return this.metaGuid;
    }

    @Override
    public void setMetaGuid( GUID metaGuid ) {
        this.metaGuid = metaGuid;
    }

    @Override
    public JSONObject toJSONObject() {
        Collection<ElementNode > children = this.fetchChildren();
        JSONObject jo = new JSONMaptron();

        for( ElementNode node : children ) {
            jo.put( node.getName(), node.toJSONObject() );
        }
        return jo;
    }

    @Override
    public JSONObject toJSONDetails() {
        return BeanColonist.DirectColonist.populate( this, ElementNode.UnbeanifiedKeys );
    }

    @Override
    public String toJSONString() {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "guid"        , this.getGuid()            ),
                new KeyValue<>( "name"        , this.getName()            )
        } );
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public Collection<ElementNode > fetchChildren() {
        return super.fetchChildren();
    }

    @Override
    public Collection<GUID > fetchChildrenGuids() {
        return super.fetchChildrenGuids();
    }

    @Override
    public void addChild( ElementNode child ) {
        super.addChild( child );
    }

    @Override
    public boolean containsChild( String childName ) {
        return super.containsChild( childName );
    }
}
