package com.walnut.odin.conduct.schedule.entity;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.walnut.odin.conduct.entity.InstanceAtlasAdjacent;
import com.walnut.odin.conduct.entity.InstanceAtlasNode;
import com.walnut.odin.task.RavenTaskInstance;

public class ScheduledTaskInstanceLineage implements Pinenut {

    protected TaskScheduleContext context;
    protected RavenTaskInstance instance;
    protected GraphNode graphNode;
    protected List<GUID> parentIds;
    protected InstanceAtlasNode instanceAtlasNode;
    protected List<InstanceAtlasAdjacent> adjacents;
    protected boolean created;

    public ScheduledTaskInstanceLineage(
            TaskScheduleContext context, RavenTaskInstance instance, GraphNode graphNode,
            List<GUID> parentIds, InstanceAtlasNode instanceAtlasNode
    ) {
        this( context, instance, graphNode, parentIds, instanceAtlasNode, true );
    }

    public ScheduledTaskInstanceLineage(
            TaskScheduleContext context, RavenTaskInstance instance, GraphNode graphNode,
            List<GUID> parentIds, InstanceAtlasNode instanceAtlasNode, boolean created
    ) {
        this.context = context;
        this.instance = instance;
        this.graphNode = graphNode;
        this.parentIds = parentIds;
        this.instanceAtlasNode = instanceAtlasNode;
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

    public GraphNode getGraphNode() {
        return this.graphNode;
    }

    public void setGraphNode( GraphNode graphNode ) {
        this.graphNode = graphNode;
    }

    public List<GUID> getParentIds() {
        return this.parentIds;
    }

    public void setParentIds( List<GUID> parentIds ) {
        this.parentIds = parentIds;
    }

    public InstanceAtlasNode getInstanceAtlasNode() {
        return this.instanceAtlasNode;
    }

    public void setInstanceAtlasNode( InstanceAtlasNode instanceAtlasNode ) {
        this.instanceAtlasNode = instanceAtlasNode;
    }

    public List<InstanceAtlasAdjacent> getAdjacents() {
        return this.adjacents;
    }

    public void setAdjacents( List<InstanceAtlasAdjacent> adjacents ) {
        this.adjacents = adjacents;
    }

    public boolean isCreated() {
        return this.created;
    }

    public void setCreated( boolean created ) {
        this.created = created;
    }
}
