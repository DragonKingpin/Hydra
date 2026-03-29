package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.InstanceEventType;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.slime.meta.TableIndex64Meta;

import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceAtlasAdjacent;
import com.walnut.odin.conduct.entity.GenericInstanceAtlasNode;
import com.walnut.odin.conduct.entity.GenericInstanceEvent;
import com.walnut.odin.conduct.entity.GenericInstanceExec;
import com.walnut.odin.conduct.entity.InstanceAtlasAdjacent;
import com.walnut.odin.conduct.entity.InstanceAtlasNode;
import com.walnut.odin.conduct.entity.InstanceEvent;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTask;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.mapper.InstanceAtlasAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceAtlasNodeMapper;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.source.ScheduleManipulator;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionElevator;

public class RavenTaskSchedulePreparator implements TaskSchedulePreparator {

    // Generate daily batches in advance (24h/Cycle)
    // 每日提前生成当日批次（24h/Cycle）
    public static final Collection<TaskScheduleCycle> DailyTaskScheduleCycles = List.of(
            TaskScheduleCycle.Month, TaskScheduleCycle.Week, TaskScheduleCycle.Day, TaskScheduleCycle.Hour
    );

    private Logger log = LoggerFactory.getLogger( this.getClass() );

    private GuidAllocator                 mGuidAllocator;
    private RavenTaskConfig               mRavenTaskConfig;
    private int                           mnScanThreadCount;
    private long                          mnScanIdWindow;

    private UniformTaskScheduler          mTaskScheduler;
    private TaskExecutionElevator         mTaskExecutionElevator;
    private UniformTaskInstrument         mUniformTaskInstrument;
    private RuntimeAtlasInstrument        mRuntimeAtlasInstrument;
    private CentralizedTaskInstrument     mCentralizedTaskInstrument;

    private RavenTaskMasterManipulator    mRavenTaskMasterManipulator;
    private TaskNodeManipulator           mTaskNodeManipulator;
    private ScheduleManipulator           mScheduleManipulator;
    private InstanceAtlasNodeMapper       mInstanceAtlasNodeMapper;
    private InstanceAtlasAdjacentMapper   mInstanceAtlasAdjacentMapper;
    private InstanceExecMapper            mInstanceExecMapper;
    private InstanceEventMapper           mInstanceEventMapper;


    private ExecutorService               mExecutorService;

    public RavenTaskSchedulePreparator( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler                = taskScheduler;
        this.mRavenTaskConfig              = taskScheduler.ravenTaskConfig();
        this.mnScanThreadCount             = this.mRavenTaskConfig.getScheduleScanThreadCount();
        this.mnScanIdWindow                = this.mRavenTaskConfig.getScheduleScanIdWindow();

        this.mRuntimeAtlasInstrument       = taskScheduler.atlasInstrument();
        this.mTaskExecutionElevator        = taskScheduler.taskExecutionElevator();
        this.mCentralizedTaskInstrument    = taskScheduler.taskInstrument();
        this.mUniformTaskInstrument        = this.mCentralizedTaskInstrument.getUniformTaskInstrument();

        this.mGuidAllocator                = this.mCentralizedTaskInstrument.getGuidAllocator();

        this.mRavenTaskMasterManipulator   = this.mCentralizedTaskInstrument.getRavenTaskMasterManipulator();
        this.mTaskNodeManipulator          = this.mRavenTaskMasterManipulator.getTaskMasterManipulator().getTaskNodeManipulator();
        this.mScheduleManipulator          = this.mRavenTaskMasterManipulator.getScheduleManipulator();
        this.mInstanceAtlasNodeMapper      = this.mScheduleManipulator.getInstanceAtlasNodeMapper();
        this.mInstanceAtlasAdjacentMapper  = this.mScheduleManipulator.getInstanceAtlasAdjacentMapper();
        this.mInstanceExecMapper           = this.mScheduleManipulator.getInstanceExecMapper();
        this.mInstanceEventMapper          = this.mScheduleManipulator.getInstanceEventMapper();

        this.mExecutorService              = Executors.newFixedThreadPool( this.mnScanThreadCount * 2 );

        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskSchedulePreparator Construction) <Done>" );
    }


    protected TaskScheduleContext prepareTaskScheduleTimeOffset( TaskElement element, LocalDateTime targetTime ) {
        TaskScheduleContext context = new TaskScheduleContext( element, targetTime );

        TaskScheduleCycle cycle = element.getScheduleCycle();
        String            cron  = element.getScheduleCron();
        if ( cycle == null ) {
            return context;
        }

        if ( cron == null || cron.isBlank() ) {
            String defaultCron = ScheduleCronHelper.generateDefaultCron( cycle );
            element.setScheduleCron( defaultCron );
            cron = defaultCron;
        }

        LocalDateTime next = element.getNextScheduleTime();
        context.setThisScheduleTime( next );
        if ( next == null ) {
            LocalDateTime firstFireTime = ScheduleCronHelper.computeNextByCron( cron, targetTime.minusSeconds( 1 ) );

            if ( firstFireTime == null ) {
                return context;
            }

            context.setThisScheduleTime( LocalDateTime.now() ); // 初始化用当前时间
            context.setNextScheduleTime( firstFireTime ); // 已经向前推进了
            element.setNextScheduleTime( firstFireTime );
            this.mTaskNodeManipulator.update( element );
            return context; // next 已经是下一次了
        }


        LocalDateTime advanced = ScheduleCronHelper.computeNextByCron( cron, next );
        if ( advanced == null ) {
            return context;
        }

        if ( !advanced.equals( next ) ) {
            element.setNextScheduleTime( advanced );
            context.setNextScheduleTime( advanced );
            //this.mTaskNodeManipulator.update( element );
        }

        return context;
    }

    protected void prepareInstance( TaskScheduleContext context, RavenTaskInstance that ) {
        LaunchFeature feature = new LaunchFeature();
        InstanceEntry it = that.getInstanceEntry();
        it.setExpectTime( context.getThisScheduleTime() ); // 先更新，后面会插入，妈的

        this.mTaskExecutionElevator.initializeInstance( that, feature );  // 这里会完成实例插入
    }

    protected void prepareInstanceLineage( TaskScheduleContext context, RavenTaskInstance instance ) {
        TaskElement element = context.getElement();
        GUID instanceGuid = instance.getInstanceEntry().getGuid();

        GraphNode graphNode = this.mRuntimeAtlasInstrument.queryGraphNodeByTaskGuid( element.getGuid() );
        List<GUID> parentIds = new ArrayList<>();

        InstanceAtlasNode instanceNode = new GenericInstanceAtlasNode();

        instanceNode.setGuid( this.mGuidAllocator.nextGUID() );
        instanceNode.setInstanceGuid( instanceGuid );
        instanceNode.setNodeName( instance.getOwnedTask().getName() );

        if ( graphNode != null ) {
            parentIds = this.mRuntimeAtlasInstrument.fetchParentIds( graphNode.getId() );
            instanceNode.setIsIsolated( parentIds == null || parentIds.isEmpty() );
        }
        else {
            instanceNode.setIsIsolated( true );
        }

        this.mInstanceAtlasNodeMapper.insert( instanceNode );

        if ( parentIds != null && !parentIds.isEmpty() ) {
            for ( GUID parentId : parentIds ) {
                InstanceAtlasAdjacent adjacent = new GenericInstanceAtlasAdjacent();
                adjacent.setGuid( this.mGuidAllocator.nextGUID() );
                adjacent.setParentGuid( parentId );
                this.mInstanceAtlasAdjacentMapper.insert( adjacent );
            }
        }
    }

    protected void persistTaskExec( TaskScheduleContext context, RavenTaskInstance instance ) {
        TaskElement element = context.getElement();
        GUID instanceGuid = instance.getInstanceEntry().getGuid();

        InstanceExec exec = new GenericInstanceExec();
        exec.setTaskGuid( element.getGuid() );
        exec.setInstanceGuid( instanceGuid );
        exec.setTaskName( instance.getOwnedTask().getName() );
        exec.setInstanceName( instance.getInstanceEntry().getInstanceName() );
        exec.setProcessorQueue( "default" );
        exec.setClusterName( "local_cluster" );
        exec.setExecState( TaskInstanceExecState.Submitted.getName() );
        exec.setCurrentRetryNumber( 0 );
        exec.setRetryTimes( instance.getInstanceEntry().getRetryCnt() );
        this.mInstanceExecMapper.insert( exec );
    }

    protected void triggerTaskEventTimeReady( TaskScheduleContext context, RavenTaskInstance instance ) {
        TaskElement element = context.getElement();
        GUID instanceGuid = instance.getInstanceEntry().getGuid();

        InstanceEvent event = new GenericInstanceEvent();
        event.setGuid( this.mGuidAllocator.nextGUID() );
        event.setTaskGuid( element.getGuid() );
        event.setInstanceGuid( instanceGuid );
        event.setInstanceName( instance.getInstanceEntry().getInstanceName() );
        event.setRetryTimes( instance.getInstanceEntry().getRetryCnt() );
        event.setCurrentRetryNumber( 0 );
        event.setEventType( instance.getTaskType() );
        event.setState( InstanceEventType.TaskTimeReady.getName() );
        event.setExecTime( LocalDateTime.now() );
        event.setEventContext( "{}" );
        this.mScheduleManipulator.getInstanceEventMapper().insert( event );
    }

    protected void prepareTaskInstances( Collection<TaskScheduleContext> contexts, LocalDateTime targetTime ) {
        for ( TaskScheduleContext context : contexts ) {
            TaskElement element = context.getElement();

            RavenTask task = this.mCentralizedTaskInstrument.constructTask( element );
            RavenTaskInstance instance = task.createInstance();

            this.prepareInstance( context, instance );
            this.prepareInstanceLineage( context, instance );
            this.persistTaskExec( context, instance );
            this.triggerTaskEventTimeReady( context, instance );
        }
    }

    protected Collection<TaskElement> prepareScheduleTasks( Collection<TaskElement> elements, LocalDateTime targetTime ) {
        if ( elements == null || elements.isEmpty() ) {
            return elements;
        }

        Collection<TaskScheduleContext> contexts = new ArrayList<>();
        for ( TaskElement element : elements ) {
            TaskScheduleContext context = this.prepareTaskScheduleTimeOffset( element, targetTime );
            contexts.add( context );
        }

        this.prepareTaskInstances( contexts, targetTime );
        Debug.traceSyn( elements );
        return elements;
    }

    @Override
    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }


    @Override
    public void prepareSchedulableTasks( Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        TableIndex64Meta range = this.mTaskNodeManipulator.selectSchedulableIdRange( cycles, targetTime );
        if ( range == null ) {
            return;
        }

        long idMin = range.getMinId();
        long idMax = range.getMaxId();
        if ( idMin <= 0 || idMax <= 0 || idMax < idMin ) {
            return;
        }

        long cursor = idMin;
        while ( cursor <= idMax ) {
            long windowStart = cursor;
            long windowEnd   = cursor + this.mnScanIdWindow - 1;

            if ( windowEnd > idMax ) {
                windowEnd = idMax;
            }

            final long finalStart = windowStart;
            final long finalEnd   = windowEnd;

            LocalDateTime finalTargetTime = targetTime;
            this.mExecutorService.submit( () -> {
                try {
                    log.info( "[TaskSchedulerLifecycle] Preparing schedulable tasks (Start: {}, End: {}) <Start>", finalStart, finalEnd );

                    Collection<TaskElement> elements = this.mTaskNodeManipulator.fetchSchedulableTasksInRange(
                            finalStart, finalEnd, cycles, finalTargetTime
                    );

                    elements = this.prepareScheduleTasks( elements, finalTargetTime );

                    log.info( "[TaskSchedulerLifecycle] Preparing schedulable tasks (Start: {}, End: {}, Size: {}) <Done>", finalStart, finalEnd, elements.size() );
                }
                catch ( Exception e ) {
                    log.error( "[TaskSchedulerLifecycle] Preparing schedulable tasks (Start: {}, End: {}) <Error>", finalStart, finalEnd, e );
                }
            } );

            cursor = windowEnd + 1;
        }
    }

    @Override
    public void prepareSchedulableTasksDaily( LocalDateTime targetTime ) {
        this.prepareSchedulableTasks( DailyTaskScheduleCycles, targetTime );
    }

    @Override
    public List<TaskElement> fetchSchedulableTasksInRange( long idMin, long idMax, Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime ) {
        return this.mTaskNodeManipulator.fetchSchedulableTasksInRange( idMin, idMax, cycles, targetTime );
    }

    @Override
    public List<TaskElement> fetchSchedulableTasksDaily( long idMin, long idMax, LocalDateTime targetTime ) {
        return this.mTaskNodeManipulator.fetchSchedulableTasksInRange( idMin, idMax, DailyTaskScheduleCycles, targetTime );
    }






    public static class TaskScheduleContext {
        protected TaskElement element;
        protected LocalDateTime targetTime;
        protected LocalDateTime nextScheduleTime;
        protected LocalDateTime thisScheduleTime;

        public TaskScheduleContext( TaskElement element, LocalDateTime targetTime ) {
            this.element = element;
            this.targetTime = targetTime;
        }

        public TaskElement getElement() {
            return this.element;
        }

        public void setElement( TaskElement element ) {
            this.element = element;
        }

        public LocalDateTime getTargetTime() {
            return this.targetTime;
        }

        public void setTargetTime( LocalDateTime targetTime ) {
            this.targetTime = targetTime;
        }

        public LocalDateTime getNextScheduleTime() {
            return this.nextScheduleTime;
        }

        public void setNextScheduleTime( LocalDateTime nextScheduleTime ) {
            this.nextScheduleTime = nextScheduleTime;
        }

        public LocalDateTime getThisScheduleTime() {
            return this.thisScheduleTime;
        }

        public void setThisScheduleTime( LocalDateTime thisScheduleTime ) {
            this.thisScheduleTime = thisScheduleTime;
        }
    }

}
