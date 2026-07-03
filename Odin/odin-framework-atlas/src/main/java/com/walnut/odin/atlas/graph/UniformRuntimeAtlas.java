package com.walnut.odin.atlas.graph;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.entity.TaskTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.walnut.odin.atlas.mapper.TaskLineageMapper;
import com.walnut.odin.conduct.entity.GenericInstanceLineageAdjacent;
import com.walnut.odin.conduct.entity.InstanceLineageAdjacent;
import com.walnut.odin.conduct.schedule.entity.DependencyBlockage;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceFrame;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceLineage;
import com.walnut.odin.conduct.schedule.entity.TaskScheduleContext;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.mapper.InstanceLineageAdjacentMapper;

public class UniformRuntimeAtlas implements RuntimeAtlasInstrument {

    private final TaskInstrument   mTaskInstrument;
    private final TaskLineageMapper mTaskLineageMapper;
    private final InstanceLineageAdjacentMapper mInstanceLineageAdjacentMapper;

    public UniformRuntimeAtlas( TaskInstrument taskInstrument, TaskLineageMapper taskLineageMapper ) {
        this.mTaskInstrument    = taskInstrument;
        this.mTaskLineageMapper = taskLineageMapper;
        this.mInstanceLineageAdjacentMapper = this.resolveInstanceLineageAdjacentMapper( taskInstrument );
    }

    @Override
    public TaskInstrument taskInstrument() {
        return this.mTaskInstrument;
    }

    @Override
    public void addDependency( GUID taskGuid, GUID parentTaskGuid ) {
        this.mTaskLineageMapper.addDependency( taskGuid, parentTaskGuid );
    }

    @Override
    public void removeDependency( GUID taskGuid, GUID parentTaskGuid ) {
        this.mTaskLineageMapper.removeDependency( taskGuid, parentTaskGuid );
    }

    @Override
    public List<GUID> fetchParentTaskGuids( GUID taskGuid ) {
        return this.mTaskLineageMapper.fetchParentTaskGuids( taskGuid );
    }

    @Override
    public List<GUID> fetchChildTaskGuids( GUID taskGuid ) {
        return this.mTaskLineageMapper.fetchChildTaskGuids( taskGuid );
    }

    @Override
    public List<TaskElement> fetchParentTasks( GUID taskGuid ) {
        return this.resolveTasks( this.fetchParentTaskGuids( taskGuid ) );
    }

    @Override
    public List<TaskElement> fetchChildTasks( GUID taskGuid ) {
        return this.resolveTasks( this.fetchChildTaskGuids( taskGuid ) );
    }

    @Override
    public TaskElement queryTaskElementByGuid( GUID taskGuid ) {
        if ( taskGuid == null ) {
            return null;
        }

        TreeNode treeNode = this.mTaskInstrument.get( taskGuid );
        if ( treeNode instanceof TaskElement ) {
            return (TaskElement) treeNode;
        }
        if ( !( treeNode instanceof TaskTreeNode ) ) {
            return null;
        }

        ElementNode elementNode = ( (TaskTreeNode) treeNode ).evinceElementNode();
        if ( elementNode == null ) {
            return null;
        }
        return elementNode.evinceTaskElement();
    }

    @Override
    public long countParents( GUID taskGuid ) {
        return this.mTaskLineageMapper.countParents( taskGuid );
    }

    @Override
    public long countChildren( GUID taskGuid ) {
        return this.mTaskLineageMapper.countChildren( taskGuid );
    }

    @Override
    public int purgeTaskLineageByTaskGuids( List<GUID> taskGuids ) {
        if ( taskGuids == null || taskGuids.isEmpty() ) {
            return 0;
        }
        return this.mTaskLineageMapper.deleteByTaskGuids( taskGuids );
    }

    protected List<TaskElement> resolveTasks( List<GUID> taskGuids ) {
        List<TaskElement> elements = new ArrayList<>();
        if ( taskGuids == null || taskGuids.isEmpty() ) {
            return elements;
        }

        for ( GUID taskGuid : taskGuids ) {
            TaskElement element = this.queryTaskElementByGuid( taskGuid );
            if ( element != null ) {
                elements.add( element );
            }
        }
        return elements;
    }

    protected InstanceLineageAdjacentMapper resolveInstanceLineageAdjacentMapper( TaskInstrument taskInstrument ) {
        if ( !( taskInstrument instanceof CentralizedTaskInstrument ) ) {
            return null;
        }
        return ( (CentralizedTaskInstrument) taskInstrument )
                .getRavenTaskMasterManipulator()
                .getScheduleManipulator()
                .getInstanceLineageAdjacentMapper();
    }

    protected InstanceInstrument instanceInstrument() {
        if ( this.mTaskInstrument instanceof CentralizedTaskInstrument ) {
            return ( (CentralizedTaskInstrument) this.mTaskInstrument ).getInstanceInstrument();
        }
        return null;
    }

    protected LocalDateTime lineageTime( ScheduledTaskInstanceFrame frame ) {
        if ( frame == null || frame.getInstance() == null || frame.getInstance().getInstanceEntry() == null ) {
            return null;
        }
        LocalDateTime businessTime = frame.getInstance().getInstanceEntry().getBusinessTime();
        if ( businessTime != null ) {
            return businessTime;
        }
        if ( frame.getContext() == null ) {
            return null;
        }
        return frame.getContext().getThisScheduleTime();
    }

    protected InstanceLineageKey lineageKey( GUID taskGuid, LocalDateTime lineageTime ) {
        return new InstanceLineageKey( taskGuid, lineageTime );
    }

    protected InstanceLineageKey lineageKey( ScheduledTaskInstanceFrame frame ) {
        if ( frame == null || frame.getContext() == null || frame.getContext().getElement() == null ) {
            return new InstanceLineageKey( null, null );
        }
        return this.lineageKey( frame.getContext().getElement().getGuid(), this.lineageTime( frame ) );
    }

    protected Map<InstanceLineageKey, ScheduledTaskInstanceFrame> indexFrames( Collection<ScheduledTaskInstanceFrame> frames ) {
        Map<InstanceLineageKey, ScheduledTaskInstanceFrame> index = new HashMap<>();
        if ( frames == null || frames.isEmpty() ) {
            return index;
        }
        for ( ScheduledTaskInstanceFrame frame : frames ) {
            InstanceLineageKey key = this.lineageKey( frame );
            if ( key.getTaskGuid() == null || key.getLineageTime() == null ) {
                continue;
            }
            index.put( key, frame );
        }
        return index;
    }

    protected InstanceEntry resolveParentInstance(
            GUID parentTaskGuid, LocalDateTime expectTime, LocalDateTime businessTime
    ) {
        InstanceInstrument instrument = this.instanceInstrument();
        if ( instrument == null || parentTaskGuid == null ) {
            return null;
        }
        if ( businessTime == null ) {
            return instrument.queryInstanceByTaskGuidAndExpectTime( parentTaskGuid, expectTime );
        }
        return instrument.queryInstanceByTaskGuidAndBusinessTime( parentTaskGuid, businessTime );
    }

    protected ScheduledTaskInstanceLineage toLineage( ScheduledTaskInstanceFrame frame, List<GUID> parentTaskGuids ) {
        TaskScheduleContext context = frame.getContext();
        RavenTaskInstance instance = frame.getInstance();
        return new ScheduledTaskInstanceLineage(
                context,
                instance,
                context.getElement().getGuid(),
                parentTaskGuids,
                frame.isCreated()
        );
    }

    protected InstanceLineageAdjacent toInstanceLineageEdge(
            ScheduledTaskInstanceFrame childFrame, GUID parentTaskGuid, InstanceEntry parentInstance
    ) {
        InstanceEntry childInstance = childFrame.getInstance().getInstanceEntry();
        GenericInstanceLineageAdjacent edge = new GenericInstanceLineageAdjacent();
        edge.setInstanceGuid( childInstance.getGuid() );
        edge.setParentInstanceGuid( parentInstance.getGuid() );
        edge.setTaskGuid( childInstance.getTaskGuid() );
        edge.setParentTaskGuid( parentTaskGuid );
        edge.setTaskName( childInstance.getTaskName() );
        edge.setParentTaskName( parentInstance.getTaskName() );
        edge.setInstanceName( childInstance.getInstanceName() );
        edge.setParentInstanceName( parentInstance.getInstanceName() );
        edge.setBusinessTime( childInstance.getBusinessTime() );
        edge.setParentBusinessTime( parentInstance.getBusinessTime() );
        return edge;
    }

    protected InstanceEntry resolveParentInstanceFromBatch(
            Map<InstanceLineageKey, ScheduledTaskInstanceFrame> frameIndex,
            GUID parentTaskGuid,
            ScheduledTaskInstanceFrame childFrame
    ) {
        LocalDateTime lineageTime = this.lineageTime( childFrame );
        if ( lineageTime == null ) {
            return null;
        }
        ScheduledTaskInstanceFrame parentFrame = frameIndex.get( this.lineageKey( parentTaskGuid, lineageTime ) );
        if ( parentFrame == null || parentFrame.getInstance() == null ) {
            return null;
        }
        return parentFrame.getInstance().getInstanceEntry();
    }

    @Override
    public Collection<ScheduledTaskInstanceLineage> freezeInstanceLineages( Collection<ScheduledTaskInstanceFrame> frames ) {
        List<ScheduledTaskInstanceLineage> lineages = new ArrayList<>();
        if ( frames == null || frames.isEmpty() ) {
            return lineages;
        }
        Map<InstanceLineageKey, ScheduledTaskInstanceFrame> frameIndex = this.indexFrames( frames );
        for ( ScheduledTaskInstanceFrame frame : frames ) {
            if ( frame == null || frame.getContext() == null || frame.getContext().getElement() == null || frame.getInstance() == null ) {
                continue;
            }
            TaskElement element = frame.getContext().getElement();
            List<GUID> parentTaskGuids = this.fetchParentTaskGuids( element.getGuid() );
            if ( parentTaskGuids == null ) {
                parentTaskGuids = new ArrayList<>();
            }
            ScheduledTaskInstanceLineage lineage = this.toLineage( frame, parentTaskGuids );
            this.purgeInstanceLineageByInstanceGuids( List.of( frame.getInstance().getInstanceEntry().getGuid() ) );
            for ( GUID parentTaskGuid : parentTaskGuids ) {
                InstanceEntry parentInstance = this.resolveParentInstanceFromBatch( frameIndex, parentTaskGuid, frame );
                if ( parentInstance == null ) {
                    parentInstance = this.resolveParentInstance(
                            parentTaskGuid,
                            frame.getContext().getThisScheduleTime(),
                            frame.getInstance().getInstanceEntry().getBusinessTime()
                    );
                }
                if ( parentInstance == null ) {
                    throw new IllegalStateException( "Cannot resolve parent instance lineage. Parent task: " + parentTaskGuid );
                }
                InstanceLineageAdjacent edge = this.toInstanceLineageEdge( frame, parentTaskGuid, parentInstance );
                lineage.getAdjacents().add( edge );
                if ( this.mInstanceLineageAdjacentMapper != null ) {
                    this.mInstanceLineageAdjacentMapper.insertIgnore( edge );
                }
            }
            lineages.add( lineage );
        }
        return lineages;
    }

    @Override
    public boolean isParentInstanceLineageResolvable(
            GUID taskGuid, GUID parentTaskGuid, LocalDateTime expectTime, LocalDateTime businessTime
    ) {
        return this.resolveParentInstance( parentTaskGuid, expectTime, businessTime ) != null;
    }

    @Override
    public List<InstanceLineageAdjacent> fetchParentInstanceEdges( GUID instanceGuid ) {
        if ( this.mInstanceLineageAdjacentMapper == null || instanceGuid == null ) {
            return List.of();
        }
        return this.mInstanceLineageAdjacentMapper.fetchParentsByInstanceGuid( instanceGuid );
    }

    @Override
    public List<InstanceLineageAdjacent> fetchChildInstanceEdges( GUID instanceGuid ) {
        if ( this.mInstanceLineageAdjacentMapper == null || instanceGuid == null ) {
            return List.of();
        }
        return this.mInstanceLineageAdjacentMapper.fetchChildrenByInstanceGuid( instanceGuid );
    }

    @Override
    public List<DependencyBlockage> fetchInstanceDependencyBlockages(
            Collection<GUID> instanceGuids, String finishedStatus
    ) {
        if ( this.mInstanceLineageAdjacentMapper == null || instanceGuids == null || instanceGuids.isEmpty() ) {
            return List.of();
        }
        return this.mInstanceLineageAdjacentMapper.fetchDependencyBlockages( instanceGuids, finishedStatus );
    }

    @Override
    public int purgeInstanceLineageByInstanceGuids( Collection<GUID> instanceGuids ) {
        if ( this.mInstanceLineageAdjacentMapper == null || instanceGuids == null || instanceGuids.isEmpty() ) {
            return 0;
        }
        return this.mInstanceLineageAdjacentMapper.deleteByInstanceGuids( instanceGuids );
    }

    protected static class InstanceLineageKey {
        private final GUID taskGuid;
        private final LocalDateTime lineageTime;

        public InstanceLineageKey( GUID taskGuid, LocalDateTime lineageTime ) {
            this.taskGuid = taskGuid;
            this.lineageTime = lineageTime;
        }

        public GUID getTaskGuid() {
            return this.taskGuid;
        }

        public LocalDateTime getLineageTime() {
            return this.lineageTime;
        }

        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( !( o instanceof InstanceLineageKey ) ) {
                return false;
            }
            InstanceLineageKey that = (InstanceLineageKey) o;
            return java.util.Objects.equals( this.taskGuid, that.taskGuid )
                    && java.util.Objects.equals( this.lineageTime, that.lineageTime );
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash( this.taskGuid, this.lineageTime );
        }
    }
}
