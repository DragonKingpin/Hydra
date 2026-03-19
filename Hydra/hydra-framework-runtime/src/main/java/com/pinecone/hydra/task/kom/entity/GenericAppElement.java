package com.pinecone.hydra.task.kom.entity;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.kom.TaskInstrument;

public class GenericAppElement extends ArchElementNode implements AppElement {
    protected String        taskType;

    public GenericAppElement() {
        super();
    }

    public GenericAppElement( Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericAppElement( Map<String, Object > joEntity, TaskInstrument taskInstrument ) {
        super( joEntity, taskInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericAppElement( TaskInstrument taskInstrument ) {
        super(taskInstrument);
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
        jo.put( "tasks", joChildren );
        return jo;
    }
}