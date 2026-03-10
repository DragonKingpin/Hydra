package com.walnut.odin.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.deploy.Server;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionElevator;

public class RavenTaskExecutionProcessor implements TaskExecutionProcessor {

    protected String                          mszName;
    protected Server                          mDeployClusterServer;
    protected String                          mszClusterPath;
    protected String                          mszClusterName;
    protected long                            mnControlClientId;
    protected boolean                         mbLocal;
    protected int                             mnPriority;
    protected boolean                         mbExclusive;

    protected TaskExecutionQueue              mTaskExecutionQueue;
    protected TaskExecutionElevator           mTaskExecutionElevator;

    protected Map<GUID, TaskLaunchContext>    mRunningProcesses;
    protected ConsumeCompromisedPolice        mConsumeCompromisedPolice;

    protected Logger                          log = LoggerFactory.getLogger( this.getClass() );

    public RavenTaskExecutionProcessor( TaskProcessorEntity processorEntity, TaskExecutionQueue queue, TaskExecutionElevator elevator ) {
        this.mszName                     = processorEntity.getName();
        this.mDeployClusterServer        = processorEntity.getDeployClusterServer();
        this.mszClusterPath              = processorEntity.getClusterPath();
        this.mszClusterName              = processorEntity.getClusterName();
        this.mnControlClientId           = processorEntity.getControlClientId();
        this.mbLocal                     = processorEntity.isLocal();
        this.mnPriority                  = processorEntity.getPriority();
        this.mbExclusive                 = processorEntity.isExclusive();
        this.mTaskExecutionQueue         = queue;
        this.mTaskExecutionElevator      = elevator;
        this.mRunningProcesses           = new ConcurrentHashMap<>();
        this.mConsumeCompromisedPolice   = ConsumeCompromisedPolice.EvictionException; // TODO, Advance
    }

    public RavenTaskExecutionProcessor( TaskProcessorEntity processorEntity, TaskExecutionElevator elevator ) {
        this( processorEntity, new GenericI32TaskQueue( processorEntity.getTaskQueueMeta() ), elevator );
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public Server getDeployClusterServer() {
        return this.mDeployClusterServer;
    }

    @Override
    public String getClusterPath() {
        return this.mszClusterPath;
    }

    @Override
    public String getClusterName() {
        return this.mszClusterName;
    }

    @Override
    public long getControlClientId() {
        return this.mnControlClientId;
    }

    @Override
    public TaskExecutionQueue getTaskExecutionQueue() {
        return this.mTaskExecutionQueue;
    }

    @Override
    public boolean isLocal() {
        return this.mbLocal;
    }

    @Override
    public int getPriority() {
        return this.mnPriority;
    }

    @Override
    public boolean isExclusive() {
        return this.mbExclusive;
    }

    @Override
    public TaskLaunchContext getTaskLaunchContextByPID( GUID pid ) {
        return this.mRunningProcesses.get( pid );
    }

    protected void prepareSysEventHandle( LaunchFeature feature ) {
        feature.withSysProcEventHandlers(new ProcessEventHandler() {
            @Override
            public void fired( EntryPointRunnable runnable, ProcessEvent event ) {
                if ( ProcessEvent.Terminated == event || ProcessEvent.Error == event ) {
                    UProcess process = runnable.ownedProcess();
                    TaskLaunchContext context = getTaskLaunchContextByPID( process.getPID() );
                    try {
                        afterProcessTerminated( process, context );
                        log.info(
                                "[ProcessSystemEventTriggered] ( ProcName:`{}`, ProcEvent:`{}`, PID:`{}`, InstanceId:`{}` ) <Scavenged>",
                                runnable.ownedProcess().getName(), event.getName(),
                                runnable.ownedProcess().getPID(), context.getTaskInstance().getId()
                        );
                    }
                    catch ( TaskDispatchException e ) {
                        // 实例错误处理在实例元数据专门回调函数中统一处理，这里不用管了
                        log.error(
                                "[ProcessSystemEventTriggered] ( ProcName:`{}`, ProcEvent:`{}`, PID:`{}`, InstanceId:`{}`, What:`{}` ) <Compromised>",
                                runnable.ownedProcess().getName(), event.getName(),
                                runnable.ownedProcess().getPID(), context.getTaskInstance().getId(),
                                e.getMessage(), e
                        );
                        handleAsyncTaskDispatchException( runnable, event, e );
                    }
                }
            }
        });
    }

    protected void handleAsyncTaskDispatchException( EntryPointRunnable runnable, ProcessEvent event, TaskDispatchException e ) {
        // TODO, 暂时默认驱逐，后面再说
    }

    @Override
    public UProcess directlyLaunch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        this.prepareSysEventHandle( feature );

        if ( this.mbLocal ) {
            return this.mTaskExecutionElevator.launchLocally( instance, feature );
        }

        return this.mTaskExecutionElevator.launchRemotely(
                instance,
                this.mnControlClientId,
                feature
        );
    }

    @Override
    public UProcess directlyElevate( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        this.prepareSysEventHandle( feature );

        if ( this.mbLocal ) {
            return this.mTaskExecutionElevator.elevateLocally( instance, feature );
        }

        return this.mTaskExecutionElevator.elevateRemotely(
                instance,
                this.mnControlClientId,
                feature
        );
    }




    protected Collection<TaskLaunchContext> subtractContext( Collection<TaskLaunchContext> source, Collection<TaskLaunchContext> consumed ) {
        if ( source == null || source.isEmpty() ) {
            return Collections.emptyList();
        }
        if ( consumed == null || consumed.isEmpty() ) {
            return source;
        }

        Set<Identification> consumedIds = new HashSet<>( consumed.size() );
        for ( TaskLaunchContext ctx : consumed ) {
            consumedIds.add( ctx.getTaskInstance().getId() );
        }

        List<TaskLaunchContext> waiting = new ArrayList<>();
        for ( TaskLaunchContext ctx : source ) {
            if ( !consumedIds.contains( ctx.getTaskInstance().getId() ) ) {
                waiting.add( ctx );
            }
        }

        return waiting;
    }

    protected void afterProcessLaunched( UProcess process, TaskLaunchContext context ) {
        context.afterProcessLaunched( process );
        this.mRunningProcesses.put( process.getPID(), context );
    }

    protected void afterProcessTerminated( UProcess process, TaskLaunchContext context ) throws TaskDispatchException {
        this.mRunningProcesses.remove( process.getPID() );
        this.shiftElevatesPipeline( List.of( context.getTaskInstance().getId() ) );
    }

    @Override
    public PipelineElevationReport prepare( Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        this.mTaskExecutionQueue.offer( contexts );

        return DefaultPipelineElevationReport.preparing(
                this,
                Collections.emptyList(),
                contexts
        );
    }

    protected PipelineElevationReport pipeOpt( Collection<TaskLaunchContext> contexts, boolean elevateOrLaunch ) throws TaskDispatchException {
        RTaskInstanceConsumer consumer = new RTaskInstanceConsumer( elevateOrLaunch );
        Collection<TaskLaunchContext> consumed = this.mTaskExecutionQueue.pipeConsume( contexts, consumer );
        List<UProcess> launched = consumer.getLaunched();

        Collection<TaskLaunchContext> waiting = this.subtractContext( contexts, consumed );
        return DefaultPipelineElevationReport.executed(
                this,
                launched,
                consumed,
                waiting
        );
    }

    @Override
    public PipelineElevationReport pipeLaunch( Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        return this.pipeOpt( contexts, false );
    }

    @Override
    public PipelineElevationReport pipeElevate( Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        return this.pipeOpt( contexts, true );
    }

    @Override
    public PipelineElevationReport recycleTerminated( Collection<Identification> terminatedIds ) {
        Collection<TaskLaunchContext> recycled = this.mTaskExecutionQueue.recycleTerminated( terminatedIds );

        return DefaultPipelineElevationReport.recycled(
                this,
                recycled
        );
    }

    @Override
    public PipelineElevationReport elevatesPending() throws TaskDispatchException {
        RTaskInstanceConsumer consumer = new RTaskInstanceConsumer( true );
        Collection<TaskLaunchContext> consumed = this.mTaskExecutionQueue.consumePending( consumer );
        List<UProcess> launched = consumer.getLaunched();

        return DefaultPipelineElevationReport.executed(
                this,
                launched,
                consumed,
                Collections.emptyList()
        );
    }

    @Override
    public PipelineElevationReport shiftElevatesPipeline( Collection<Identification> terminatedIds ) throws TaskDispatchException {
        RTaskInstanceConsumer consumer = new RTaskInstanceConsumer( true );
        Collection<TaskLaunchContext> consumed = this.mTaskExecutionQueue.shiftPipeline( terminatedIds, consumer );
        List<UProcess> launched = consumer.getLaunched();

        return DefaultPipelineElevationReport.executed(
                this,
                launched,
                consumed,
                Collections.emptyList()
        );
    }


    protected class RTaskInstanceConsumer implements TaskInstanceConsumer {

        public List<UProcess> launched;

        public boolean elevateOrLaunch;

        public RTaskInstanceConsumer( boolean elevateOrLaunch ) {
            this.launched = new ArrayList<>();
            this.elevateOrLaunch = elevateOrLaunch;
        }

        @Override
        public void tryConsume( TaskLaunchContext context ) throws TaskConsumeException {
            try {
                UProcess proc;
                if ( this.elevateOrLaunch ) {
                    proc = directlyElevate( context.getTaskInstance(), context.getLaunchFeature() );
                }
                else {
                    proc = directlyLaunch( context.getTaskInstance(), context.getLaunchFeature() );
                }
                this.launched.add( proc );
                afterProcessLaunched( proc, context );
            }
            catch ( InstanceLaunchException e ) {
                log.error( "Error during shift pipeline, what:'{}' ", e.getMessage(), e );
                throw new TaskConsumeException( e );
            }
        }

        @Override
        public ConsumeCompromisedPolice compromisedPolice() {
            return mConsumeCompromisedPolice;
        }

        public List<UProcess> getLaunched() {
            return this.launched;
        }
    }

}