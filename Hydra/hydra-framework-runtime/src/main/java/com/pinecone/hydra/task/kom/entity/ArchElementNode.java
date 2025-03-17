package com.pinecone.hydra.task.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.ArchTaskFamilyMeta;
import com.pinecone.hydra.task.kom.TasksInstrument;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ArchElementNode extends ArchTaskFamilyMeta implements ElementNode{

    protected long                       enumId;

    protected GUIDImperialTrieNode distributedTreeNode;
    protected TasksInstrument tasksInstrument;

    public ArchElementNode() {
        super();
    }

    public ArchElementNode( Map<String, Object > joEntity ) {
        super( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public ArchElementNode( Map<String, Object > joEntity, TasksInstrument servicesInstrument ) {
        super( joEntity );
        this.apply( servicesInstrument );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public ArchElementNode( TasksInstrument servicesInstrument ) {
        this.apply( servicesInstrument );
    }

    public void apply( TasksInstrument servicesInstrument ) {
        this.tasksInstrument = servicesInstrument;
        GuidAllocator guidAllocator = this.tasksInstrument.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID() );
    }

    @Override
    public ArchElementNode apply( Map<String, Object > joEntity ) {
        super.apply( joEntity );
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );

        return this;
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
    public void setPrimaryImplLang( String primaryImplLang ) {
        this.primaryImplLang = primaryImplLang;
    }

    @Override
    public void setExtraInformation( String extraInformation ) {
        this.extraInformation = extraInformation;
    }

    @Override
    public void setLevel( String level ) {
        this.level = level;
    }

    @Override
    public void setDescription( String description ) {
        this.description = description;
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


    protected List<ElementNode > fetchChildren() {
        List<GUID > guids = this.fetchChildrenGuids();
        List<ElementNode > elementNodes = new ArrayList<>();
        for( GUID guid : guids ){
            ElementNode elementNode = (ElementNode) this.tasksInstrument.get( guid );
            elementNodes.add( elementNode );
        }
        return elementNodes;
    }

    protected List<GUID > fetchChildrenGuids() {
        return this.tasksInstrument.fetchChildrenGuids( this.getGuid() );
    }

    protected void addChild( ElementNode child ) {
        GUID childId;
        boolean bContainsChild = this.containsChild( child.getName() );
        if( bContainsChild ) {
            return;
        }
        else {
            childId = this.tasksInstrument.put( child );
        }


        this.tasksInstrument.affirmOwnedNode( this.guid, childId );
    }

    protected boolean containsChild( String childName ) {
        return this.tasksInstrument.containsChild( this.guid, childName );
    }

    @Override
    public JSONObject toJSONObject() {
        return BeanColonist.DirectColonist.populate( this, ServoElement.UnbeanifiedKeys );
    }


}
