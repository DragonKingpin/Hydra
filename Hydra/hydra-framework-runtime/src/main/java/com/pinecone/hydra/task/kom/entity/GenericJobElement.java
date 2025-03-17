package com.pinecone.hydra.task.kom.entity;

import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.kom.ServiceInstrument;

public class GenericJobElement extends ArchServoElement implements JobElement {
    protected String                     deploymentMethod;

    public GenericJobElement() {
        super();
    }

    public GenericJobElement(Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericJobElement(Map<String, Object > joEntity, ServiceInstrument serviceInstrument) {
        super( joEntity, serviceInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericJobElement(ServiceInstrument serviceInstrument) {
        super(serviceInstrument);
    }

    @Override
    public String getDeploymentMethod() {
        return this.deploymentMethod;
    }

    @Override
    public void setDeploymentMethod( String deploymentMethod ) {
        this.deploymentMethod = deploymentMethod;
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
        List<ElementNode > children = this.fetchChildren();
        JSONObject jo         = BeanColonist.DirectColonist.populate( this, UnbeanifiedKeys);
        JSONObject joChildren = new JSONMaptron();

        for( ElementNode node : children ) {
            joChildren.put( node.getName(), node.toJSONObject() );
        }
        jo.put( "services", joChildren );
        return jo;
    }
}