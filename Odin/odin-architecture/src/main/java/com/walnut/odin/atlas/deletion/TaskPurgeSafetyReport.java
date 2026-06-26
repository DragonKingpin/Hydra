package com.walnut.odin.atlas.deletion;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class TaskPurgeSafetyReport implements Pinenut {

    protected GUID                           taskGuid;
    protected List<GUID>                     taskGuids = new ArrayList<>();
    protected List<TaskPurgeLineageRef>      lineageRefs = new ArrayList<>();
    protected List<TaskPurgeLineageRef>      parentRefs = new ArrayList<>();
    protected List<TaskPurgeLineageRef>      childRefs = new ArrayList<>();
    protected List<TaskPurgeRunningInstance> runningInstances = new ArrayList<>();
    protected boolean                        forceOfflineRequired;
    protected boolean                        hasWarnings;
    protected boolean                        hasBlockingChildren;
    protected boolean                        purgeAllowed;
    protected String                         message;

    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.taskGuid = taskGuid;
    }

    public List<GUID> getTaskGuids() {
        return this.taskGuids;
    }

    public void setTaskGuids( List<GUID> taskGuids ) {
        this.taskGuids = taskGuids;
    }

    public List<TaskPurgeLineageRef> getLineageRefs() {
        return this.lineageRefs;
    }

    public void setLineageRefs( List<TaskPurgeLineageRef> lineageRefs ) {
        this.lineageRefs = lineageRefs;
    }

    public List<TaskPurgeLineageRef> getParentRefs() {
        return this.parentRefs;
    }

    public void setParentRefs( List<TaskPurgeLineageRef> parentRefs ) {
        this.parentRefs = parentRefs;
    }

    public List<TaskPurgeLineageRef> getChildRefs() {
        return this.childRefs;
    }

    public void setChildRefs( List<TaskPurgeLineageRef> childRefs ) {
        this.childRefs = childRefs;
    }

    public List<TaskPurgeRunningInstance> getRunningInstances() {
        return this.runningInstances;
    }

    public void setRunningInstances( List<TaskPurgeRunningInstance> runningInstances ) {
        this.runningInstances = runningInstances;
    }

    public boolean isForceOfflineRequired() {
        return this.forceOfflineRequired;
    }

    public void setForceOfflineRequired( boolean forceOfflineRequired ) {
        this.forceOfflineRequired = forceOfflineRequired;
    }

    public boolean isHasWarnings() {
        return this.hasWarnings;
    }

    public void setHasWarnings( boolean hasWarnings ) {
        this.hasWarnings = hasWarnings;
    }

    public boolean isHasBlockingChildren() {
        return this.hasBlockingChildren;
    }

    public void setHasBlockingChildren( boolean hasBlockingChildren ) {
        this.hasBlockingChildren = hasBlockingChildren;
    }

    public boolean isPurgeAllowed() {
        return this.purgeAllowed;
    }

    public void setPurgeAllowed( boolean purgeAllowed ) {
        this.purgeAllowed = purgeAllowed;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }
}
