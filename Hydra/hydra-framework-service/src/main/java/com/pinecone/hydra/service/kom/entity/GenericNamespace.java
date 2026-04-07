package com.pinecone.hydra.service.kom.entity;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.service.kom.GenericNamespaceRules;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public class GenericNamespace extends ArchElementNode implements Namespace {
    protected GUID                        rulesGUID;

    protected GUID                        metaGuid;

    protected GUIDImperialTrieNode distributedTreeNode;

    protected GenericNamespaceRules       classificationRules;

    protected ServiceNamespaceManipulator namespaceManipulator;


    public GenericNamespace() {
        super();
    }

    public GenericNamespace( Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericNamespace( Map<String, Object > joEntity, ServiceInstrument serviceInstrument) {
        super( joEntity, serviceInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericNamespace( ServiceInstrument serviceInstrument) {
        super(serviceInstrument);
    }

    public GenericNamespace(ServiceInstrument serviceInstrument, ServiceNamespaceManipulator namespaceManipulator ) {
        this(serviceInstrument);
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
    public GenericNamespaceRules getClassificationRules() {
        return this.classificationRules;
    }

    @Override
    public void setClassificationRules( GenericNamespaceRules classificationRules ) {
        this.classificationRules = classificationRules;
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
    public GUID getRulesGUID() {
        return this.rulesGUID;
    }

    @Override
    public void setRulesGUID( GUID rulesGUID ) {
        this.rulesGUID = rulesGUID;
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
        return BeanColonist.DirectColonist.populate( this, UnbeanifiedKeys);
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
