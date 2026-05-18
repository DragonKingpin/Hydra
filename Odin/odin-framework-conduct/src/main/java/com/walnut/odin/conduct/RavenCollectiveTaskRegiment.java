package com.walnut.odin.conduct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.ProcessManagerSystema;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.walnut.odin.conduct.entity.LaunchedContext;
import com.walnut.odin.conduct.entity.RegimentJoinRequest;
import com.walnut.odin.conduct.entity.RegimentJoinResponse;
import com.walnut.odin.conduct.lifecycle.KernelTaskInstanceLifecycleInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleInstrument;
import com.walnut.odin.dispatch.RavenTaskDispatcher;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.dispatch.TaskQueueMeta;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.server.RavenRemoteProcessManagerServer;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.troll.GenericRavenTask;
import com.walnut.odin.task.RavenTask;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;
import com.walnut.odin.task.troll.TrollTaskExecutionLauncher;


public class RavenCollectiveTaskRegiment implements CollectiveTaskRegiment {

    protected Hydrogen                      mSystem;

    protected Logger                        mLogger;

    protected CentralizedTaskInstrument     mTaskInstrument;

    protected ProcessManager                mProcessManager;

    protected RemoteProcessManagerServer    mRemoteProcessManagerServer;

    protected TaskExecutionLauncher         mTaskExecutionLauncher;

    protected TaskDispatcher                mTaskDispatcher;

    protected TaskInstanceLifecycleInstrument mTaskInstanceLifecycleInstrument;





    protected void traceWelcomeInfo() {
        Tracer console = this.mSystem.console();
        console.getOut().print( "---------------------------------------------------------------\n" );
        console.getOut().print( "\u001B[34mRaven Odin Collective Task Regiment\u001B[0m\n" );
        console.getOut().print( "\u001B[34mCentralized task lifecycle management and deployment system.\u001B[0m\n" );
        console.getOut().print( "---------------------------------------------------------------\n" );

        this.infoLifecycle( "Welcome to use Odin task orchestration system.", LogStatuses.StatusReady );
    }

    protected void prepare_odin_collective_regiment_subsystem() {
        this.infoLifecycle( "Preparing Odin`s army, constructing task-regiment.", LogStatuses.StatusStart );

        this.mTaskInstanceLifecycleInstrument = new KernelTaskInstanceLifecycleInstrument(
                this.mTaskInstrument.getInstanceInstrument(),
                this.mTaskInstrument.getRavenTaskMasterManipulator().getScheduleManipulator().getInstanceEventMapper()
        );
        this.infoLifecycle( "TaskInstanceLifecycleInstrument: `" + this.mTaskInstanceLifecycleInstrument.getClass().getName() + "` <Constructed>.", LogStatuses.StatusDone );

        this.mTaskExecutionLauncher = new TrollTaskExecutionLauncher( this );
        this.infoLifecycle( "TaskExecutionLauncher: `" + this.mTaskExecutionLauncher.getClass().getName() + "` <Constructed>.", LogStatuses.StatusDone );

        this.mTaskDispatcher = new RavenTaskDispatcher( this );
        this.infoLifecycle( "TaskDispatcher: `" + this.mTaskDispatcher.getClass().getName() + "` <Constructed>.", LogStatuses.StatusDone );

        this.traceWelcomeInfo();
    }

    public RavenCollectiveTaskRegiment( Hydrogen system, CentralizedTaskInstrument taskInstrument, ProcessManager processManager, RemoteProcessManagerServer remoteProcessManagerServer ) {
        this.mSystem                      = system;
        this.mTaskInstrument              = taskInstrument;
        this.mProcessManager              = processManager;
        this.mRemoteProcessManagerServer  = remoteProcessManagerServer;
        this.mLogger                      = LoggerFactory.getLogger( "OdinCollectiveTaskRegiment" );

        this.prepare_odin_collective_regiment_subsystem();
    }

    public RavenCollectiveTaskRegiment( ProcessManagerSystema system, CentralizedTaskInstrument taskInstrument, RemoteProcessManagerServer remoteProcessManagerServer ) {
        this( system, taskInstrument, system.processManager(), remoteProcessManagerServer );
    }

    public RavenCollectiveTaskRegiment( ProcessManagerSystema system, CentralizedTaskInstrument taskInstrument, UlfServer rpcServer ) {
        this( system, taskInstrument, system.processManager(), new RavenRemoteProcessManagerServer( system.processManager(), rpcServer ) );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public RemoteProcessManagerServer remoteProcessManagerServer() {
        return this.mRemoteProcessManagerServer;
    }

    @Override
    public void startRemoteProcessServer() throws RemoteProcessServiceRPCException {
        this.mRemoteProcessManagerServer.startService();

        ProcessorLifecycleController controller = new ProcessorLifecycleController( this );
        DuplexAppointServer duplexAppointServer = this.mRemoteProcessManagerServer.duplexAppointServer();
        duplexAppointServer.registerController( controller );
        duplexAppointServer.compile( ProcessorLifecycleIface.class, false );
    }

    @Override
    public ProcessManager processManager() {
        return this.mProcessManager;
    }

    @Override
    public CentralizedTaskInstrument taskInstrument() {
        return this.mTaskInstrument;
    }

    @Override
    public TaskExecutionLauncher taskExecutionLauncher() {
        return this.mTaskExecutionLauncher;
    }

    @Override
    public TaskDispatcher taskDispatcher() {
        return this.mTaskDispatcher;
    }

    @Override
    public TaskInstanceLifecycleInstrument taskInstanceLifecycleInstrument() {
        return this.mTaskInstanceLifecycleInstrument;
    }

    @Override
    public RavenTask queryTaskByPath( String path ) {
        GUID objGuid = this.mTaskInstrument.queryGUIDByPath( path );
        if ( objGuid == null ) {
            return null;
        }

        return this.getTaskByGuid( objGuid );
    }

    @Override
    public RavenTask getTaskByGuid( GUID taskGuid ) {
        TreeNode treeNode = this.mTaskInstrument.get( taskGuid );
        if ( !(treeNode instanceof TaskElement) ) {
            throw new IllegalArgumentException( "Object node `" + taskGuid + "` is not task." );
        }
        TaskElement taskElement = (TaskElement) treeNode;

        return this.mTaskInstrument.constructTask( taskElement );
    }

    @Override
    public RavenTask createTask( TaskElement taskElement, Identification serviceId ) {
        RavenTask task = this.mTaskInstrument.createTask( taskElement, serviceId );
        return task;
    }

    @Override
    public RavenTask affirmTask( String path, Identification serviceId, TaskElement metaInfos ) {
        TaskElement taskElement = this.mTaskInstrument.affirmTask( path ,metaInfos );
        Debug.trace(taskElement);
    /*    taskElement.setActuallyPriority( metaInfos.getActuallyPriority() );
        taskElement.setDeploymentMethod( metaInfos.getDeploymentMethod() );
        taskElement.setEnable( metaInfos.isEnable());
        taskElement.setDryRun( metaInfos.isDryRun() );
        taskElement.setPriority( metaInfos.getPriority() );
        taskElement.setResourceType( metaInfos.getResourceType() );
        taskElement.setScheduleCycle( metaInfos.getScheduleCycle() );
        taskElement.setScheduleType( metaInfos.getScheduleType() );
        taskElement.setScheduleTypeCode( metaInfos.getScheduleTypeCode() );
        taskElement.setScheduleCycleCode( metaInfos.getScheduleCycleCode() );
        taskElement.setType( metaInfos.getType() );
        taskElement.setImagePath( metaInfos.getImagePath() );
        taskElement.setName( metaInfos.getName() );
        taskElement.setGuid( metaInfos.getGuid() );*/
        /*this.mTaskInstrument.get(  taskElement.getGuid());*/
        /*this.mTaskInstrument.query(  taskElement.getGuid() );*/

     /*  String newPath = this.mTaskInstrument.getPath(  taskElement.getGuid());
        Debug.trace(newPath);*/

        this.updateTaskMeta( taskElement );
        String newPath = this.mTaskInstrument.getPath( taskElement.getGuid() );
        Debug.trace(newPath);
        return this.mTaskInstrument.constructTask( taskElement, serviceId );
    }

    @Override
    public void purgeTask( GUID guid ) {
        GenericRavenTask  task = (GenericRavenTask) this.getTaskByGuid( guid );
        task.removeInstance( guid );
        this.mTaskInstrument.remove( guid );
    }

    public void updateTaskMeta( RavenTask task ) {
        this.updateTaskMeta( task.getTaskElement() );
    }

    public void updateTaskMeta( TaskElement taskElement ) {
        this.mTaskInstrument.update( taskElement );
    }




    protected LaunchedContext launch0( GUID taskGuid, LaunchFeature feature, boolean launch ) throws InstanceLaunchException, TaskDispatchException {
        RavenTask task = this.getTaskByGuid( taskGuid );
        RavenTaskInstance instance = task.createInstance();

        UProcess process;
        if ( launch ) {
            process = this.mTaskDispatcher.launch( instance, feature );
        }
        else {
            process = this.mTaskDispatcher.create( instance, feature );
        }

        LaunchedContext context = new LaunchedContext( process, instance );
        return context;
    }

    @Override
    public LaunchedContext create( GUID taskGuid, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
        return this.launch0( taskGuid, feature, false );
    }

    @Override
    public LaunchedContext launch( GUID taskGuid, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
        return this.launch0( taskGuid, feature, true );
    }


    @Override
    public LaunchedContext create( String path, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
        GUID taskGuid = this.mTaskInstrument.queryGUIDByPath( path );
        if ( taskGuid == null ) {
            throw new IllegalArgumentException( "Task `" + path + "` is not task." );
        }
        return this.create( taskGuid, feature );
    }

    @Override
    public LaunchedContext launch( String path, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
        GUID taskGuid = this.mTaskInstrument.queryGUIDByPath( path );
        if ( taskGuid == null ) {
            throw new IllegalArgumentException( "Task `" + path + "` is not task." );
        }
        return this.launch( taskGuid, feature );
    }

    @Override
    public RegimentJoinResponse invokeJoinRegiment( RegimentJoinRequest request ) {
        RegimentJoinResponse response = new RegimentJoinResponse();
        try {
            TaskProcessorEntity entity = this.mTaskDispatcher.registerProcessor( request.getNodeName(), request.getClientId() );

            response.setGuid( entity.getGuid().toString() );
            response.setName( entity.getName() );
            response.setClusterPath( entity.getClusterPath() );
            response.setClusterName( entity.getClusterName() );
            response.setControlClientId( entity.getControlClientId() );
            response.setPriority( entity.getPriority() );

            TaskQueueMeta queueMeta = entity.getTaskQueueMeta();
            response.setQueueName( queueMeta.getName() );
            response.setQueueMaxCapacity( queueMeta.getMaxCapacity() );
            response.setQueueMinCapacity( queueMeta.getMinCapacity() );
            response.setQueueRuntimeInstanceCapacity( queueMeta.getRuntimeInstanceCapacity() );

            this.mLogger.info(
                    "[NewProcessorRegister] " +
                    "( name:`{}`, clientId:`{}`, clusterPath:`{}`, priority:`{}`, queueMaxCapacity:`{}`, runtimeCapacity:`{}` ) " +
                    "<Done>",

                    entity.getName(), entity.getControlClientId(), entity.getClusterPath(), entity.getPriority(),
                    queueMeta.getMaxCapacity(), queueMeta.getRuntimeInstanceCapacity()
            );
        }
        catch ( IllegalArgumentException e ) {
            response.setErrorMsg( e.getMessage() );
        }

        return response;
    }


}
