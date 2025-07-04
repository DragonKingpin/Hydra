package com.walnut.odin.conduct;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.troll.GenericRavenTask;
import com.walnut.odin.task.RavenTask;
import com.walnut.odin.ups.conduct.TaskRegimentDomain;


public class RavenCollectiveTaskRegiment implements CollectiveTaskRegiment {

    protected Hydrogen                      mSystem;

    protected CentralizedTaskInstrument     mTaskInstrument;

    protected ProcessManager                mProcessManager;

    protected RemoteProcessManagerServer    mRemoteProcessManagerServer;

    protected TaskRegimentDomain            mTaskRegimentDomain;


    public RavenCollectiveTaskRegiment( Hydrogen system, CentralizedTaskInstrument taskInstrument ) {
        this.mSystem         = system;
        this.mTaskInstrument = taskInstrument;
    }

    @Override
    public RemoteProcessManagerServer remoteProcessManagerServer() {
        return this.mRemoteProcessManagerServer;
    }

    @Override
    public ProcessManager processManager() {
        return this.mProcessManager;
    }

    public RavenTask queryTaskByPath( String path ) {
        GUID objGuid = this.mTaskInstrument.queryGUIDByPath( path );
        if ( objGuid == null ) {
            return null;
        }

        return this.getTaskByGuid( objGuid );
    }

    public RavenTask getTaskByGuid( GUID taskGuid ) {
        TreeNode treeNode = this.mTaskInstrument.get( taskGuid );
        if ( !(treeNode instanceof TaskElement) ) {
            throw new IllegalArgumentException( "Object node `" + taskGuid + "` is not task." );
        }
        TaskElement taskElement = (TaskElement) treeNode;

        return this.constructTask( taskElement );
    }

    public RavenTask constructTask( TaskElement taskElement ) {
        return this.constructTask( taskElement, null );
    }

    public RavenTask constructTask( TaskElement taskElement, Identification serviceId ) {
        if ( serviceId == null ) {
            //serviceId = taskElement.
        }
        RavenTask task = new GenericRavenTask( this.mTaskInstrument, serviceId, taskElement );

        return task;
    }

    public RavenTask createTask( TaskElement taskElement, Identification serviceId ) {
        this.mTaskInstrument.put( taskElement );
        RavenTask task = this.constructTask( taskElement, serviceId );


        return task;
    }

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
        return this.constructTask( taskElement, serviceId );
    }


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


}
