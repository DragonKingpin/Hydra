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
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceLineageAdjacent;
import com.walnut.odin.conduct.entity.GenericInstanceLineageNode;
import com.walnut.odin.conduct.entity.InstanceLineageAdjacent;
import com.walnut.odin.conduct.entity.InstanceLineageNode;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceFrame;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceLineage;
import com.walnut.odin.conduct.schedule.entity.TaskScheduleContext;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.mapper.InstanceLineageAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceLineageNodeMapper;

public class RavenTaskInstanceLineageFreezer implements TaskInstanceLineageFreezer {

    protected GuidAllocator mGuidAllocator;
    protected RuntimeAtlasInstrument mRuntimeAtlasInstrument;
    protected InstanceLineageNodeMapper mInstanceLineageNodeMapper;
    protected InstanceLineageAdjacentMapper mInstanceLineageAdjacentMapper;

    public RavenTaskInstanceLineageFreezer(
            GuidAllocator guidAllocator, RuntimeAtlasInstrument runtimeAtlasInstrument,
            InstanceLineageNodeMapper instanceLineageNodeMapper, InstanceLineageAdjacentMapper instanceLineageAdjacentMapper
    ) {
        this.mGuidAllocator               = guidAllocator;
        this.mRuntimeAtlasInstrument      = runtimeAtlasInstrument;
        this.mInstanceLineageNodeMapper     = instanceLineageNodeMapper;
        this.mInstanceLineageAdjacentMapper = instanceLineageAdjacentMapper;
    }

    protected ScheduledTaskInstanceLineage prepareInstanceLineageFrame( ScheduledTaskInstanceFrame frame ) {
        TaskScheduleContext context = frame.getContext();
        RavenTaskInstance instance = frame.getInstance();
        TaskElement element = context.getElement();
        GUID instanceGuid = instance.getInstanceEntry().getGuid();

        GUID taskGuid = element.getGuid();
        List<GUID> parentTaskGuids = this.mRuntimeAtlasInstrument.fetchParentTaskGuids( taskGuid );
        if ( parentTaskGuids == null ) {
            parentTaskGuids = new ArrayList<>();
        }

        InstanceLineageNode instanceNode = this.mInstanceLineageNodeMapper.queryByInstanceGuid( instanceGuid );
        if ( instanceNode == null ) {
            instanceNode = new GenericInstanceLineageNode();
            instanceNode.setGuid( this.mGuidAllocator.nextGUID() );
            instanceNode.setInstanceGuid( instanceGuid );
            instanceNode.setNodeName( instance.getOwnedTask().getName() );
        }

        return new ScheduledTaskInstanceLineage( context, instance, taskGuid, parentTaskGuids, instanceNode, frame.isCreated() );
    }

    protected InstanceLineageNode resolveParentInstanceLineageNode(
            ScheduledTaskInstanceLineage lineage, GUID parentTaskGuid, Map<GUID, ScheduledTaskInstanceLineage> lineageByTaskGuid
    ) {
        ScheduledTaskInstanceLineage inMemory = lineageByTaskGuid.get( parentTaskGuid );
        if ( inMemory != null ) {
            return inMemory.getInstanceLineageNode();
        }

        TaskElement parentElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid( parentTaskGuid );
        if ( parentElement == null ) {
            return null;
        }

        TaskScheduleContext context = lineage.getContext();
        RavenTaskInstance instance = lineage.getInstance();
        LocalDateTime businessTime = instance.getInstanceEntry().getBusinessTime();
        if ( businessTime == null ) {
            return this.mInstanceLineageNodeMapper.queryByTaskGuidAndExpectTime( parentElement.getGuid(), context.getThisScheduleTime() );
        }
        return this.mInstanceLineageNodeMapper.queryByTaskGuidAndBusinessTime( parentElement.getGuid(), businessTime );
    }

    protected void prepareInstanceLineages( Collection<ScheduledTaskInstanceLineage> lineages ) {
        Map<GUID, ScheduledTaskInstanceLineage> lineageByTaskGuid = new HashMap<>();
        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            GUID taskGuid = lineage.getTaskGuid();
            if ( taskGuid != null ) {
                lineageByTaskGuid.put( taskGuid, lineage );
            }
        }

        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            List<GUID> parentTaskGuids = lineage.getParentTaskGuids();
            List<InstanceLineageAdjacent> adjacents = new ArrayList<>();

            if ( parentTaskGuids != null && !parentTaskGuids.isEmpty() ) {
                for ( GUID parentTaskGuid : parentTaskGuids ) {
                    InstanceLineageNode parentNode = this.resolveParentInstanceLineageNode(
                            lineage, parentTaskGuid, lineageByTaskGuid
                    );
                    if ( parentNode == null ) {
                        throw new IllegalStateException( "Cannot resolve parent instance lineage node. Parent task: " + parentTaskGuid );
                    }

                    InstanceLineageAdjacent adjacent = new GenericInstanceLineageAdjacent();
                    adjacent.setGuid( lineage.getInstanceLineageNode().getGuid() );
                    adjacent.setParentGuid( parentNode.getGuid() );
                    adjacents.add( adjacent );
                }
            }

            lineage.getInstanceLineageNode().setSource( adjacents.isEmpty() );
            lineage.setAdjacents( adjacents );
        }
    }

    protected void persistInstanceLineageNodes( Collection<ScheduledTaskInstanceLineage> lineages ) {
        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            InstanceLineageNode node = lineage.getInstanceLineageNode();
            InstanceLineageNode existing = this.mInstanceLineageNodeMapper.queryByInstanceGuid( node.getInstanceGuid() );
            if ( existing == null ) {
                this.mInstanceLineageNodeMapper.insert( node );
            }
            else {
                this.mInstanceLineageNodeMapper.updateSourceByGuid( existing.getGuid(), node.isSource() );
                this.mInstanceLineageAdjacentMapper.deleteByGuid( existing.getGuid() );
            }
        }
    }

    protected void persistInstanceLineageAdjacents( Collection<ScheduledTaskInstanceLineage> lineages ) {
        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            for ( InstanceLineageAdjacent adjacent : lineage.getAdjacents() ) {
                long existing = this.mInstanceLineageAdjacentMapper.countByGuidAndParentGuid(
                        adjacent.getGuid(), adjacent.getParentGuid()
                );
                if ( existing <= 0 ) {
                    this.mInstanceLineageAdjacentMapper.insert( adjacent );
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
        this.persistInstanceLineageNodes( lineages );
        this.persistInstanceLineageAdjacents( lineages );

        return lineages;
    }
}
