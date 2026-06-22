package com.walnut.odin.conduct.schedule.entity;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.conduct.entity.InstanceLineageAdjacent;
import com.walnut.odin.conduct.entity.InstanceLineageNode;
import com.walnut.odin.task.RavenTaskInstance;

public class ScheduledTaskInstanceLineage implements Pinenut {

    protected TaskScheduleContext context;
    protected RavenTaskInstance instance;
    protected GUID taskGuid;
    protected List<GUID> parentTaskGuids;
    protected InstanceLineageNode instanceLineageNode;
    protected List<InstanceLineageAdjacent> adjacents;
    protected boolean created;

    public ScheduledTaskInstanceLineage(
            TaskScheduleContext context, RavenTaskInstance instance, GUID taskGuid,
            List<GUID> parentTaskGuids, InstanceLineageNode instanceLineageNode
    ) {
        this( context, instance, taskGuid, parentTaskGuids, instanceLineageNode, true );
    }

    public ScheduledTaskInstanceLineage(
            TaskScheduleContext context, RavenTaskInstance instance, GUID taskGuid,
            List<GUID> parentTaskGuids, InstanceLineageNode instanceLineageNode, boolean created
    ) {
        this.context = context;
        this.instance = instance;
        this.taskGuid = taskGuid;
        this.parentTaskGuids = parentTaskGuids;
        this.instanceLineageNode = instanceLineageNode;
        this.adjacents = new ArrayList<>();
        this.created = created;
    }

    public TaskScheduleContext getContext() {
        return this.context;
    }

    public void setContext( TaskScheduleContext context ) {
        this.context = context;
    }

    public RavenTaskInstance getInstance() {
        return this.instance;
    }

    public void setInstance( RavenTaskInstance instance ) {
        this.instance = instance;
    }

    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.taskGuid = taskGuid;
    }

    public List<GUID> getParentTaskGuids() {
        return this.parentTaskGuids;
    }

    public void setParentTaskGuids( List<GUID> parentTaskGuids ) {
        this.parentTaskGuids = parentTaskGuids;
    }

    public InstanceLineageNode getInstanceLineageNode() {
        return this.instanceLineageNode;
    }

    public void setInstanceLineageNode( InstanceLineageNode instanceLineageNode ) {
        this.instanceLineageNode = instanceLineageNode;
    }

    public List<InstanceLineageAdjacent> getAdjacents() {
        return this.adjacents;
    }

    public void setAdjacents( List<InstanceLineageAdjacent> adjacents ) {
        this.adjacents = adjacents;
    }

    public boolean isCreated() {
        return this.created;
    }

    public void setCreated( boolean created ) {
        this.created = created;
    }
}
