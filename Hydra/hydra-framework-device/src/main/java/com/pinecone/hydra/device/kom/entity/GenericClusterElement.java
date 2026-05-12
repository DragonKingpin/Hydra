package com.pinecone.hydra.device.kom.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.device.kom.DeviceInstrument;

public class GenericClusterElement extends ArchElementNode implements ClusterElement {
    protected String        taskType;

    public GenericClusterElement() {
        super();
    }

    public GenericClusterElement(Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericClusterElement(Map<String, Object > joEntity, DeviceInstrument deviceInstrument) {
        super( joEntity, deviceInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericClusterElement(DeviceInstrument deviceInstrument) {
        super(deviceInstrument);
    }
    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
        jo.put( "devicements", joChildren );
        return jo;
    }
}