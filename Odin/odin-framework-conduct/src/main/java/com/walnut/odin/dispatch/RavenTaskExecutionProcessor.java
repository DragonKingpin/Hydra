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
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.proc.ProcessRemoteEventHandler;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

public class RavenTaskExecutionProcessor implements TaskExecutionProcessor {

    protected String                          mszName;
    protected Server                          mDeployClusterServer;
    protected String                          mszClusterPath;
    protected String                          mszClusterName;
    protected long                            mnControlClientId;
    protected String                          mszExecCaps;
    protected boolean                         mbLocal;
    protected int                             mnPriority;
    protected boolean                         mbExclusive;

    protected TaskExecutionQueue              mTaskExecutionQueue;
    protected TaskExecutionLauncher           mTaskExecutionLauncher;
    protected InstanceExecMapper              mInstanceExecMapper;

    protected Map<GUID, TaskLaunchContext>    mRunningProcesses;
    protected Set<GUID>                       mTerminatedProcesses;
    protected Set<GUID>                       mRemoteEventHandledProcesses;
    protected ConsumeCompromisedPolice        mConsumeCompromisedPolice;

    protected Logger                          log = LoggerFactory.getLogger( this.getClass() );

    public RavenTaskExecutionProcessor(
            TaskProcessorEntity processorEntity,
            TaskExecutionQueue queue,
            TaskExecutionLauncher launcher,
            InstanceExecMapper instanceExecMapper
    ) {
        this.mszName                     = processorEntity.getName();
        this.mDeployClusterServer        = processorEntity.getDeployClusterServer();
        this.mszClusterPath              = processorEntity.getClusterPath();
        this.mszClusterName              = processorEntity.getClusterName();
        this.mnControlClientId           = processorEntity.getControlClientId();
        this.mszExecCaps                 = processorEntity.getExecCaps();
        this.mbLocal                     = processorEntity.isLocal();
        this.mnPriority                  = processorEntity.getPriority();
        this.mbExclusive                 = processorEntity.isExclusive();
        this.mTaskExecutionQueue         = queue;
        this.mTaskExecutionLauncher      = launcher;
        this.mInstanceExecMapper         = instanceExecMapper;
        this.mRunningProcesses           = new ConcurrentHashMap<>();
        this.mTerminatedProcesses        = ConcurrentHashMap.newKeySet();
        this.mRemoteEventHandledProcesses = ConcurrentHashMap.newKeySet();
        this.mConsumeCompromisedPolice   = ConsumeCompromisedPolice.EvictionIgnore;
    }

    public RavenTaskExecutionProcessor( TaskProcessorEntity processorEntity, TaskExecutionLauncher launcher ) {
        this( processorEntity, new GenericI32TaskQueue( processorEntity.getTaskQueueMeta() ), launcher, null );
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
    public String getExecCaps() {
        return this.mszExecCaps;
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

    @Override
    public int getRunningSize() {
        return  this.mRunningProcesses.size();
    }

    @Override
    public int getWaitingSize() {
        return this.mTaskExecutionQueue.waitingSize();
    }

    protected void prepareSysEventHandle( LaunchFeature feature ) {
        this.prepareSysEventHandle( feature, null );
    }

    protected void prepareSysEventHandle( LaunchFeature feature, TaskLaunchContext boundContext ) {
        feature.withSysProcEventHandlers(new ProcessEventHandler() {
            @Override
            public void fired( EntryPointRunnable runnable, UProcessStatus event ) {
                if ( UProcessStatus.Terminated == event || UProcessStatus.Error == event ) {
                    UProcess process = runnable.ownedProcess();
                    TaskLaunchContext context = boundContext;
                    if ( context == null ) {
                        context = getTaskLaunchContextByPID( process.getPID() );
                    }
                    Identification instanceId = null;
                    if ( context != null ) {
                        instanceId = context.getTaskInstance().getId();
                    }
                    try {
                        afterProcessTerminated( process, context );
                        log.info(
                                "[ProcessSystemEventTriggered] ( ProcName:`{}`, ProcEvent:`{}`, PID:`{}`, InstanceId:`{}` ) <Scavenged>",
                                runnable.ownedProcess().getName(), event.getName(),
                                runnable.ownedProcess().getPID(),
                                instanceId
                        );
                    }
                    catch ( TaskDispatchException e ) {
                        // 实例错误处理在实例元数据专门回调函数中统一处理，这里不用管了
                        log.error(
                                "[ProcessSystemEventTriggered] ( ProcName:`{}`, ProcEvent:`{}`, PID:`{}`, InstanceId:`{}`, What:`{}` ) <Compromised>",
                                runnable.ownedProcess().getName(), event.getName(),
                                runnable.ownedProcess().getPID(),
                                instanceId,
                                e.getMessage(), e
                        );
                        handleAsyncTaskDispatchException( runnable, event, e );
                    }
                }
            }
        });
    }

    protected void handleAsyncTaskDispatchException( EntryPointRunnable runnable, UProcessStatus event, TaskDispatchException e ) {
        // TODO, 暂时默认驱逐，后面再说
    }

    @Override
    public UProcess directlyCreate( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        this.prepareSysEventHandle( feature );

        if ( this.mbLocal ) {
            return this.mTaskExecutionLauncher.createLocally( instance, feature );
        }

        return this.mTaskExecutionLauncher.createRemotely(
                instance,
                this.mnControlClientId,
                feature
        );
    }

    @Override
    public UProcess directlyCreatePrepared( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        this.prepareSysEventHandle( feature );

        if ( this.mbLocal ) {
            return this.mTaskExecutionLauncher.createPreparedLocally( instance, feature );
        }

        return this.mTaskExecutionLauncher.createPreparedRemotely(
                instance,
                this.mnControlClientId,
                feature
        );
    }

    @Override
    public UProcess directlyLaunch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        this.prepareSysEventHandle( feature );

        if ( this.mbLocal ) {
            return this.mTaskExecutionLauncher.launchLocally( instance, feature );
        }

        return this.mTaskExecutionLauncher.launchRemotely(
                instance,
                this.mnControlClientId,
                feature
        );
    }

    @Override
    public UProcess directlyLaunchPrepared( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        this.prepareSysEventHandle( feature );

        if ( this.mbLocal ) {
            return this.mTaskExecutionLauncher.launchPreparedLocally( instance, feature );
        }

        return this.mTaskExecutionLauncher.launchPreparedRemotely(
                instance,
                this.mnControlClientId,
                feature
        );
    }

    @Override
    public UProcess directlyStartPrepared( TaskLaunchContext context ) throws InstanceLaunchException {
        LaunchFeature feature = context.getLaunchFeature();
        this.prepareSysEventHandle( feature, context );

        if ( this.mbLocal ) {
            return this.mTaskExecutionLauncher.startLocally(
                    context.getTaskInstance(), context.getLaunchedProcess(), feature
            );
        }

        return this.mTaskExecutionLauncher.startRemotely(
                context.getTaskInstance(), context.getLaunchedProcess(), this.mnControlClientId, feature
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
        if ( process == null || context == null ) {
            return;
        }
        context.afterProcessLaunched( process );
        this.recordLaunchedProcessGuid( process, context );
        if ( process.getPID() != null && this.mTerminatedProcesses.contains( process.getPID() ) ) {
            this.mTaskExecutionQueue.markTerminated( context.getTaskInstance().getId() );
            log.info(
                    "[ProcessLaunchRace] ( ProcName:`{}`, PID:`{}`, InstanceId:`{}` ) <AlreadyTerminated>",
                    process.getName(),
                    process.getPID(),
                    context.getTaskInstance().getId()
            );
            return;
        }
        this.mRunningProcesses.put( process.getPID(), context );
        this.prepareRemoteEventHandle( process, context );
    }

    protected void recordLaunchedProcessGuid( UProcess process, TaskLaunchContext context ) {
        if ( process == null || process.getPID() == null || context == null || context.getTaskInstance() == null ) {
            return;
        }

        InstanceEntry entry = context.getTaskInstance().getInstanceEntry();
        if ( entry == null || entry.getGuid() == null ) {
            return;
        }
        if ( this.mInstanceExecMapper == null ) {
            return;
        }

        this.mInstanceExecMapper.updateProcessGuidByInstanceGuidAndRetry(
                entry.getGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                process.getPID()
        );
    }

    protected void prepareRemoteEventHandle( UProcess process, TaskLaunchContext context ) {
        if ( !( process instanceof RemoteProcess ) ) {
            return;
        }
        if ( process.getPID() == null || !this.mRemoteEventHandledProcesses.add( process.getPID() ) ) {
            return;
        }

        RemoteProcess remoteProcess = (RemoteProcess) process;
        remoteProcess.addRemoteEventHandler(new ProcessRemoteEventHandler() {
            @Override
            public void fired( long pmClientId, UProcessStatus event, Object caused ) {
                if ( event != UProcessStatus.Terminated && event != UProcessStatus.Error ) {
                    return;
                }

                try {
                    afterProcessTerminated( process, context );
                    log.info(
                            "[RemoteProcessEventTriggered] ( ProcName:`{}`, ProcEvent:`{}`, PID:`{}`, InstanceId:`{}` ) <Scavenged>",
                            process.getName(),
                            event.getName(),
                            process.getPID(),
                            context.getTaskInstance().getId()
                    );
                }
                catch ( TaskDispatchException e ) {
                    log.error(
                            "[RemoteProcessEventTriggered] ( ProcName:`{}`, ProcEvent:`{}`, PID:`{}`, InstanceId:`{}`, What:`{}` ) <Compromised>",
                            process.getName(),
                            event.getName(),
                            process.getPID(),
                            context.getTaskInstance().getId(),
                            e.getMessage(),
                            e
                    );
                }
            }
        });
    }

    protected void afterProcessTerminated( UProcess process, TaskLaunchContext context ) throws TaskDispatchException {
        if ( process == null || process.getPID() == null ) {
            return;
        }
        this.mRunningProcesses.remove( process.getPID() );
        if ( context == null ) {
            log.warn(
                    "[ProcessSystemEventTriggered] ( ProcName:`{}`, PID:`{}` ) has no launch context. <SkippedPipelineShift>",
                    process.getName(), process.getPID()
            );
            return;
        }
        if ( !this.mTerminatedProcesses.add( process.getPID() ) ) {
            return;
        }
        this.shiftLaunchsPipeline( List.of( context.getTaskInstance().getId() ) );
    }

    @Override
    public PipelineLaunchReport prepare( Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        this.mTaskExecutionQueue.offer( contexts );

        return DefaultPipelineLaunchReport.preparing(
                this,
                Collections.emptyList(),
                contexts
        );
    }

    protected PipelineLaunchReport pipeOpt(
            Collection<TaskLaunchContext> contexts, boolean directlyLaunch, boolean preparedCreation
    ) throws TaskDispatchException {
        RTaskInstanceConsumer consumer = new RTaskInstanceConsumer( directlyLaunch, preparedCreation );
        Collection<TaskLaunchContext> consumed = this.mTaskExecutionQueue.pipeConsume( contexts, consumer );
        List<UProcess> launched = consumer.getLaunched();

        Collection<TaskLaunchContext> waiting = this.subtractContext( contexts, consumed );
        return DefaultPipelineLaunchReport.executed(
                this,
                launched,
                consumed,
                waiting
        );
    }

    @Override
    public PipelineLaunchReport pipeCreate(Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        return this.pipeOpt( contexts, false, false );
    }

    @Override
    public PipelineLaunchReport pipeCreatePrepared(Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        return this.pipeOpt( contexts, false, true );
    }

    @Override
    public PipelineLaunchReport pipeLaunch(Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        return this.pipeOpt( contexts, true, false );
    }

    @Override
    public PipelineLaunchReport pipeLaunchPrepared(Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        return this.pipeOpt( contexts, true, true );
    }

    @Override
    public PipelineLaunchReport pipeStartPrepared(Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        if ( contexts == null || contexts.isEmpty() ) {
            return DefaultPipelineLaunchReport.executed(
                    this,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }

        List<UProcess> launched = new ArrayList<>();
        List<TaskLaunchContext> consumed = new ArrayList<>();
        List<TaskLaunchContext> waiting = new ArrayList<>();
        for ( TaskLaunchContext context : contexts ) {
            try {
                UProcess process = this.directlyStartPrepared( context );
                if ( process == null ) {
                    waiting.add( context );
                    continue;
                }
                this.afterProcessLaunched( process, context );
                launched.add( process );
                consumed.add( context );
            }
            catch ( InstanceLaunchException e ) {
                log.error( "Error during start prepared process, what:'{}' ", e.getMessage(), e );
                this.mTaskExecutionQueue.markTerminated( context.getTaskInstance().getId() );
                consumed.add( context );
            }
        }

        return DefaultPipelineLaunchReport.executed(
                this,
                launched,
                consumed,
                waiting
        );
    }

    @Override
    public PipelineLaunchReport recycleTerminated(Collection<Identification> terminatedIds ) {
        Collection<TaskLaunchContext> recycled = this.mTaskExecutionQueue.recycleTerminated( terminatedIds );

        return DefaultPipelineLaunchReport.recycled(
                this,
                recycled
        );
    }

    @Override
    public PipelineLaunchReport launchsPending() throws TaskDispatchException {
        RTaskInstanceConsumer consumer = new RTaskInstanceConsumer( true );
        Collection<TaskLaunchContext> consumed = this.mTaskExecutionQueue.consumePending( consumer );
        List<UProcess> launched = consumer.getLaunched();

        return DefaultPipelineLaunchReport.executed(
                this,
                launched,
                consumed,
                Collections.emptyList()
        );
    }

    @Override
    public PipelineLaunchReport shiftLaunchsPipeline(Collection<Identification> terminatedIds ) throws TaskDispatchException {
        RTaskInstanceConsumer consumer = new RTaskInstanceConsumer( true );
        Collection<TaskLaunchContext> consumed = this.mTaskExecutionQueue.shiftPipeline( terminatedIds, consumer );
        List<UProcess> launched = consumer.getLaunched();

        return DefaultPipelineLaunchReport.executed(
                this,
                launched,
                consumed,
                Collections.emptyList()
        );
    }


    protected class RTaskInstanceConsumer implements TaskInstanceConsumer {

        public List<UProcess> launched;

        public boolean directlyLaunch;
        public boolean preparedCreation;

        public RTaskInstanceConsumer( boolean directlyLaunch ) {
            this( directlyLaunch, false );
        }

        public RTaskInstanceConsumer( boolean directlyLaunch, boolean preparedCreation ) {
            this.launched = new ArrayList<>();
            this.directlyLaunch = directlyLaunch;
            this.preparedCreation = preparedCreation;
        }

        @Override
        public void tryConsume( TaskLaunchContext context ) throws TaskConsumeException {
            try {
                UProcess proc;
                if ( this.directlyLaunch && this.preparedCreation ) {
                    proc = directlyLaunchPrepared( context.getTaskInstance(), context.getLaunchFeature() );
                }
                else if ( this.directlyLaunch ) {
                    proc = directlyLaunch( context.getTaskInstance(), context.getLaunchFeature() );
                }
                else if ( this.preparedCreation ) {
                    proc = directlyCreatePrepared( context.getTaskInstance(), context.getLaunchFeature() );
                }
                else {
                    proc = directlyCreate( context.getTaskInstance(), context.getLaunchFeature() );
                }
                if ( proc == null ) {
                    throw new TaskConsumeException(
                            new InstanceLaunchException( "Process launcher returned null process." )
                    );
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
