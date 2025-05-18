package com.pinecone.hydra.deploy.kom.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.deploy.kom.DeployInstrument;

public class GenericClusterElement extends ArchElementNode implements ClusterElement {
    protected String        taskType;

    public GenericClusterElement() {
        super();
    }

    public GenericClusterElement(Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericClusterElement(Map<String, Object > joEntity, DeployInstrument deployInstrument) {
        super( joEntity, deployInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericClusterElement(DeployInstrument deployInstrument) {
        super(deployInstrument);
    }

    @Override
    public String getType() {
        return this.taskType;
    }

    @Override
    public void setType( String taskType ) {
        this.taskType = taskType;
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
        if( child instanceof FolderElement ) {
            throw new IllegalArgumentException( "Foisting `FolderElement` into application node is not accepted." );
        }
        super.addChild( child );
    }

    @Override
    public boolean containsChild( String childName ) {
        return super.containsChild( childName );
    }

    @Override
    public JSONObject toJSONObject() {
        Collection<ElementNode > children = this.fetchChildren();
        JSONObject jo         = BeanColonist.DirectColonist.populate( this, UnbeanifiedKeys );
        JSONObject joChildren = new JSONMaptron();

        for( ElementNode node : children ) {
            joChildren.put( node.getName(), node.toJSONObject() );
        }
        jo.put( "deployments", joChildren );
        return jo;
    }
}