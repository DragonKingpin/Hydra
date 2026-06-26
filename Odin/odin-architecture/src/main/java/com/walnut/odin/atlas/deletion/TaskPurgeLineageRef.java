package com.walnut.odin.atlas.deletion;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class TaskPurgeLineageRef implements Pinenut {

    protected GUID   taskGuid;
    protected GUID   relatedTaskGuid;
    protected String direction;
    protected String taskName;
    protected String taskPath;
    protected String relatedTaskName;
    protected String relatedTaskPath;

    public TaskPurgeLineageRef() {
    }

    public TaskPurgeLineageRef( GUID taskGuid, GUID relatedTaskGuid, String direction ) {
        this.taskGuid = taskGuid;
        this.relatedTaskGuid = relatedTaskGuid;
        this.direction = direction;
    }

    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.taskGuid = taskGuid;
    }

    public GUID getRelatedTaskGuid() {
        return this.relatedTaskGuid;
    }

    public void setRelatedTaskGuid( GUID relatedTaskGuid ) {
        this.relatedTaskGuid = relatedTaskGuid;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection( String direction ) {
        this.direction = direction;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName( String taskName ) {
        this.taskName = taskName;
    }

    public String getTaskPath() {
        return this.taskPath;
    }

    public void setTaskPath( String taskPath ) {
        this.taskPath = taskPath;
    }

    public String getRelatedTaskName() {
        return this.relatedTaskName;
    }

    public void setRelatedTaskName( String relatedTaskName ) {
        this.relatedTaskName = relatedTaskName;
    }

    public String getRelatedTaskPath() {
        return this.relatedTaskPath;
    }

    public void setRelatedTaskPath( String relatedTaskPath ) {
        this.relatedTaskPath = relatedTaskPath;
    }
}
