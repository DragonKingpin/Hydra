package com.pinecone.hydra.deploy.kom.entity;

import java.util.List;
import java.util.Map;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.GenericNamespaceRules;
import com.pinecone.hydra.deploy.kom.source.DeployNamespaceManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public class GenericNamespace extends ArchElementNode implements Namespace {
    protected GUID                        rulesGUID;

    protected GUID                        metaGuid;

    protected GUIDImperialTrieNode distributedTreeNode;

    protected GenericNamespaceRules       classificationRules;

    protected DeployNamespaceManipulator namespaceManipulator;


    public GenericNamespace() {
        super();
    }

    public GenericNamespace( Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericNamespace( Map<String, Object > joEntity, DeployInstrument deployInstrument) {
        super( joEntity, deployInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericNamespace( DeployInstrument deployInstrument) {
        super(deployInstrument);
    }

    public GenericNamespace(DeployInstrument deployInstrument, DeployNamespaceManipulator namespaceManipulator ) {
        this(deployInstrument);
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
        List<ElementNode > children = this.fetchChildren();
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
    public List<ElementNode > fetchChildren() {
        return super.fetchChildren();
    }

    @Override
    public List<GUID > fetchChildrenGuids() {
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
