package com.walnut.odin.conduct.schedule.lineage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceAtlasAdjacent;
import com.walnut.odin.conduct.entity.GenericInstanceAtlasNode;
import com.walnut.odin.conduct.entity.InstanceAtlasAdjacent;
import com.walnut.odin.conduct.entity.InstanceAtlasNode;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceFrame;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceLineage;
import com.walnut.odin.conduct.schedule.entity.TaskScheduleContext;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.mapper.InstanceAtlasAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceAtlasNodeMapper;

public class RavenTaskInstanceLineageFreezer implements TaskInstanceLineageFreezer {

    protected GuidAllocator mGuidAllocator;
    protected RuntimeAtlasInstrument mRuntimeAtlasInstrument;
    protected InstanceAtlasNodeMapper mInstanceAtlasNodeMapper;
    protected InstanceAtlasAdjacentMapper mInstanceAtlasAdjacentMapper;

    public RavenTaskInstanceLineageFreezer(
            GuidAllocator guidAllocator, RuntimeAtlasInstrument runtimeAtlasInstrument,
            InstanceAtlasNodeMapper instanceAtlasNodeMapper, InstanceAtlasAdjacentMapper instanceAtlasAdjacentMapper
    ) {
        this.mGuidAllocator               = guidAllocator;
        this.mRuntimeAtlasInstrument      = runtimeAtlasInstrument;
        this.mInstanceAtlasNodeMapper     = instanceAtlasNodeMapper;
        this.mInstanceAtlasAdjacentMapper = instanceAtlasAdjacentMapper;
    }

    protected ScheduledTaskInstanceLineage prepareInstanceLineageFrame( ScheduledTaskInstanceFrame frame ) {
        TaskScheduleContext context = frame.getContext();
        RavenTaskInstance instance = frame.getInstance();
        TaskElement element = context.getElement();
        GUID instanceGuid = instance.getInstanceEntry().getGuid();

        GraphNode graphNode = this.mRuntimeAtlasInstrument.queryGraphNodeByTaskGuid( element.getGuid() );
        List<GUID> parentIds = new ArrayList<>();

        InstanceAtlasNode instanceNode = this.mInstanceAtlasNodeMapper.queryByInstanceGuid( instanceGuid );
        if ( instanceNode == null ) {
            instanceNode = new GenericInstanceAtlasNode();
            instanceNode.setGuid( this.mGuidAllocator.nextGUID() );
            instanceNode.setInstanceGuid( instanceGuid );
            instanceNode.setNodeName( instance.getOwnedTask().getName() );
        }

        if ( graphNode != null ) {
            parentIds = this.mRuntimeAtlasInstrument.fetchParentIds( graphNode.getId() );
        }

        return new ScheduledTaskInstanceLineage( context, instance, graphNode, parentIds, instanceNode, frame.isCreated() );
    }

    protected InstanceAtlasNode resolveParentInstanceAtlasNode(
            ScheduledTaskInstanceLineage lineage, GUID parentGraphNodeGuid, Map<GUID, ScheduledTaskInstanceLineage> lineageByGraphNodeGuid
    ) {
        ScheduledTaskInstanceLineage inMemory = lineageByGraphNodeGuid.get( parentGraphNodeGuid );
        if ( inMemory != null ) {
            return inMemory.getInstanceAtlasNode();
        }

        TaskElement parentElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid( parentGraphNodeGuid );
        if ( parentElement == null ) {
            return null;
        }

        TaskScheduleContext context = lineage.getContext();
        RavenTaskInstance instance = lineage.getInstance();
        LocalDateTime businessTime = instance.getInstanceEntry().getBusinessTime();
        if ( businessTime == null ) {
            return this.mInstanceAtlasNodeMapper.queryByTaskGuidAndExpectTime( parentElement.getGuid(), context.getThisScheduleTime() );
        }
        return this.mInstanceAtlasNodeMapper.queryByTaskGuidAndBusinessTime( parentElement.getGuid(), businessTime );
    }

    protected void prepareInstanceLineages( Collection<ScheduledTaskInstanceLineage> lineages ) {
        Map<GUID, ScheduledTaskInstanceLineage> lineageByGraphNodeGuid = new HashMap<>();
        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            GraphNode graphNode = lineage.getGraphNode();
            if ( graphNode != null ) {
                lineageByGraphNodeGuid.put( graphNode.getId(), lineage );
            }
        }

        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            List<GUID> parentIds = lineage.getParentIds();
            List<InstanceAtlasAdjacent> adjacents = new ArrayList<>();

            if ( parentIds != null && !parentIds.isEmpty() ) {
                for ( GUID parentId : parentIds ) {
                    InstanceAtlasNode parentNode = this.resolveParentInstanceAtlasNode(
                            lineage, parentId, lineageByGraphNodeGuid
                    );
                    if ( parentNode == null ) {
                        throw new IllegalStateException( "Cannot resolve parent instance atlas node. Parent graph node: " + parentId );
                    }

                    InstanceAtlasAdjacent adjacent = new GenericInstanceAtlasAdjacent();
                    adjacent.setGuid( lineage.getInstanceAtlasNode().getGuid() );
                    adjacent.setParentGuid( parentNode.getGuid() );
                    adjacents.add( adjacent );
                }
            }

            lineage.getInstanceAtlasNode().setSource( adjacents.isEmpty() );
            lineage.setAdjacents( adjacents );
        }
    }

    protected void persistInstanceAtlasNodes( Collection<ScheduledTaskInstanceLineage> lineages ) {
        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            InstanceAtlasNode node = lineage.getInstanceAtlasNode();
            InstanceAtlasNode existing = this.mInstanceAtlasNodeMapper.queryByInstanceGuid( node.getInstanceGuid() );
            if ( existing == null ) {
                this.mInstanceAtlasNodeMapper.insert( node );
            }
            else {
                this.mInstanceAtlasNodeMapper.updateSourceByGuid( existing.getGuid(), node.isSource() );
            }
        }
    }

    protected void persistInstanceAtlasAdjacents( Collection<ScheduledTaskInstanceLineage> lineages ) {
        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            for ( InstanceAtlasAdjacent adjacent : lineage.getAdjacents() ) {
                long existing = this.mInstanceAtlasAdjacentMapper.countByGuidAndParentGuid(
                        adjacent.getGuid(), adjacent.getParentGuid()
                );
                if ( existing <= 0 ) {
                    this.mInstanceAtlasAdjacentMapper.insert( adjacent );
                }
            }
        }
    }

    @Override
    public Collection<ScheduledTaskInstanceLineage> freeze( Collection<ScheduledTaskInstanceFrame> frames ) {
        List<ScheduledTaskInstanceLineage> lineages = new ArrayList<>();
        if ( frames == null || frames.isEmpty() ) {
            return lineages;
        }

        for ( ScheduledTaskInstanceFrame frame : frames ) {
            lineages.add( this.prepareInstanceLineageFrame( frame ) );
        }

        this.prepareInstanceLineages( lineages );
        this.persistInstanceAtlasNodes( lineages );
        this.persistInstanceAtlasAdjacents( lineages );

        return lineages;
    }
}
