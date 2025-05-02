package com.walnut.odin.task.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanColonist;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.task.kom.TaskFamilyNode;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public class GenericRavenTaskElement implements RavenTaskElement {

    protected TaskElement taskElement;

    protected RavenTaskMeta ravenTaskMeta;

    public GenericRavenTaskElement( TaskElement taskElement, RavenTaskMeta ravenTaskMeta ) {
        this.taskElement   = taskElement;
        this.ravenTaskMeta = ravenTaskMeta;
    }

    @Override
    public RavenTaskElement from( TaskElement taskElement ) {
        this.taskElement = taskElement;
        return this;
    }

    @Override
    public String getMetaType() {
        return TaskElement.class.getSimpleName();
    }

    @Override
    public String getImagePath() {
        return this.taskElement.getImagePath();
    }

    @Override
    public void setImagePath( String path ) {
        this.taskElement.setImagePath( path );
    }

    @Override
    public String getType() {
        return this.taskElement.getType();
    }

    @Override
    public void setType( String type ) {
        this.taskElement.setType( type );
    }

    @Override
    public String getDeploymentMethod() {
        return this.taskElement.getDeploymentMethod();
    }

    @Override
    public void setDeploymentMethod( String deploymentMethod ) {
        this.taskElement.setDeploymentMethod( deploymentMethod );
    }

    @Override
    public String getResourceType() {
        return this.taskElement.getResourceType();
    }

    @Override
    public void setResourceType( String resourceType ) {
        this.taskElement.setResourceType( resourceType );
    }

    @Override
    public short getPriority() {
        return this.taskElement.getPriority();
    }

    @Override
    public void setPriority( short priority ) {
        this.taskElement.setPriority( priority );
    }

    @Override
    public short getActuallyPriority() {
        return this.taskElement.getActuallyPriority();
    }

    @Override
    public void setActuallyPriority( short priority ) {
        this.taskElement.setActuallyPriority( priority );
    }

    @Override
    public boolean isDryRun() {
        return this.taskElement.isDryRun();
    }

    @Override
    public void setDryRun( boolean dryRun ) {
        this.taskElement.setDryRun( dryRun );
    }

    @Override
    public int getScheduleTypeCode() {
        return this.taskElement.getScheduleTypeCode();
    }

    @Override
    public void setScheduleTypeCode( int scheduleTypeCode ) {
        this.taskElement.setScheduleTypeCode( scheduleTypeCode );
    }

    @Override
    public boolean isEnable() {
        return this.taskElement.isEnable();
    }

    @Override
    public void setEnable( boolean enable ) {
        this.taskElement.setEnable( enable );
    }

    @Override
    public GUIDImperialTrieNode getDistributedTreeNode() {
        return this.taskElement.getDistributedTreeNode();
    }

    @Override
    public void setDistributedTreeNode( GUIDImperialTrieNode distributedTreeNode ) {
        this.taskElement.setDistributedTreeNode( distributedTreeNode );
    }

    @Override
    public JSONObject toJSONObject() {
        return BeanColonist.DirectColonist.populate( this, UnbeanifiedKeys );
    }

    @Override
    public GUID getMetaGuid() {
        return this.taskElement.getMetaGuid();
    }

    @Override
    public void setMetaGuid( GUID metaGuid ) {
        this.taskElement.setMetaGuid( metaGuid );
    }

    @Override
    public String getKomPath() {
        return this.taskElement.getKomPath();
    }

    @Override
    public String getName() {
        return this.taskElement.getName();
    }

    @Override
    public GUID getGuid() {
        return this.taskElement.getGuid();
    }

    @Override
    public long getEnumId() {
        return this.taskElement.getEnumId();
    }

    @Override
    public void setEnumId( long id ) {
        this.taskElement.setEnumId( id );
    }

    @Override
    public void setName( String name ) {
        this.taskElement.setName( name );
    }

    @Override
    public void setGuid( GUID guid ) {
        this.taskElement.setGuid( guid );
    }

    @Override
    public String getScenario() {
        return this.taskElement.getScenario();
    }

    @Override
    public void setScenario( String scenario ) {
        this.taskElement.setScenario( scenario );
    }

    @Override
    public String getMarshallingArchitecture() {
        return this.taskElement.getMarshallingArchitecture();
    }

    @Override
    public void setMarshallingArchitecture( String marshallingArchitecture ) {
        this.taskElement.setMarshallingArchitecture( marshallingArchitecture );
    }

    @Override
    public String getExtraInformation() {
        return this.taskElement.getExtraInformation();
    }

    @Override
    public void setExtraInformation( String extraInformation ) {
        this.taskElement.setExtraInformation( extraInformation );
    }

    @Override
    public String getDescription() {
        return this.taskElement.getDescription();
    }

    @Override
    public void setDescription( String description ) {
        this.taskElement.setDescription( description );
    }

    @Override
    public TaskFamilyNode apply( Map<String, Object> joEntity ) {
        return this.taskElement.apply( joEntity );
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.taskElement.getCreateTime();
    }

    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.taskElement.setCreateTime( createTime );
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.taskElement.getUpdateTime();
    }

    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.taskElement.setUpdateTime( updateTime );
    }

    @Override
    public RavenTaskMeta getExtraMeta() {
        return this.ravenTaskMeta;
    }

    @Override
    public void setExtraMeta( RavenTaskMeta meta ) {
        this.ravenTaskMeta = meta;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

}
