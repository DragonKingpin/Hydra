package com.walnut.odin.conduct.entity;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;

public class GenericInstanceLineageAdjacent implements InstanceLineageAdjacent {
    protected GUID instanceGuid;
    protected GUID parentInstanceGuid;
    protected GUID taskGuid;
    protected GUID parentTaskGuid;
    protected String taskName;
    protected String parentTaskName;
    protected String instanceName;
    protected String parentInstanceName;
    protected LocalDateTime businessTime;
    protected LocalDateTime parentBusinessTime;

    public GenericInstanceLineageAdjacent() {
    }

    public GenericInstanceLineageAdjacent( Map<String, Object> joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    @Override
    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    @Override
    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    @Override
    public GUID getParentInstanceGuid() {
        return this.parentInstanceGuid;
    }

    @Override
    public void setParentInstanceGuid( GUID parentInstanceGuid ) {
        this.parentInstanceGuid = parentInstanceGuid;
    }

    @Override
    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    @Override
    public void setTaskGuid( GUID taskGuid ) {
        this.taskGuid = taskGuid;
    }

    @Override
    public GUID getParentTaskGuid() {
        return this.parentTaskGuid;
    }

    @Override
    public void setParentTaskGuid( GUID parentTaskGuid ) {
        this.parentTaskGuid = parentTaskGuid;
    }

    @Override
    public String getTaskName() {
        return this.taskName;
    }

    @Override
    public void setTaskName( String taskName ) {
        this.taskName = taskName;
    }

    @Override
    public String getParentTaskName() {
        return this.parentTaskName;
    }

    @Override
    public void setParentTaskName( String parentTaskName ) {
        this.parentTaskName = parentTaskName;
    }

    @Override
    public String getInstanceName() {
        return this.instanceName;
    }

    @Override
    public void setInstanceName( String instanceName ) {
        this.instanceName = instanceName;
    }

    @Override
    public String getParentInstanceName() {
        return this.parentInstanceName;
    }

    @Override
    public void setParentInstanceName( String parentInstanceName ) {
        this.parentInstanceName = parentInstanceName;
    }

    @Override
    public LocalDateTime getBusinessTime() {
        return this.businessTime;
    }

    @Override
    public void setBusinessTime( LocalDateTime businessTime ) {
        this.businessTime = businessTime;
    }

    @Override
    public LocalDateTime getParentBusinessTime() {
        return this.parentBusinessTime;
    }

    @Override
    public void setParentBusinessTime( LocalDateTime parentBusinessTime ) {
        this.parentBusinessTime = parentBusinessTime;
    }
}
