package com.walnut.odin.atlas.deletion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.signal.ProcSignal;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.ibatis.AppNodeMapper;
import com.pinecone.hydra.task.ibatis.InstanceNodeMapper;
import com.pinecone.hydra.task.ibatis.TaskNamespaceMapper;
import com.pinecone.hydra.task.ibatis.TaskNodeMapper;
import com.pinecone.hydra.task.ibatis.TaskNodeOwnerMapper;
import com.pinecone.hydra.task.ibatis.TaskPathCacheMapper;
import com.pinecone.hydra.task.ibatis.TaskTreeMapper;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.atlas.mapper.TaskLineageMapper;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.mapper.transaction.OdinMappingTransactionScope;
import com.walnut.odin.proc.entity.RemoteProcessSignalResult;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecAuditMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.mapper.InstanceLineageAdjacentMapper;
import com.walnut.odin.task.mapper.PatrolWatchdogLogMapper;
import com.walnut.odin.task.mapper.TaskInstanceOperationLogMapper;
import com.walnut.odin.task.source.ScheduleManipulator;

public class RavenTaskPurgeService implements TaskPurgeService {

    protected static final String LINEAGE_DIRECTION_PARENT = "Parent";
    protected static final String LINEAGE_DIRECTION_CHILD  = "Child";

    protected CentralizedTaskInstrument primaryTask;
    protected RuntimeAtlasInstrument    atlasInstrument;
    protected RemoteProcessManagerServer remoteProcessManagerServer;

    public RavenTaskPurgeService(
            CentralizedTaskInstrument primaryTask,
            RuntimeAtlasInstrument atlasInstrument,
            RemoteProcessManagerServer remoteProcessManagerServer
    ) {
        this.primaryTask = primaryTask;
        this.atlasInstrument = atlasInstrument;
        this.remoteProcessManagerServer = remoteProcessManagerServer;
    }

    @Override
    public TaskPurgeSafetyReport check( TaskPurgeRequest request ) {
        GUID taskGuid = this.requireTaskGuid( request );
        if ( this.primaryTask.get( taskGuid ) == null ) {
            throw new TaskPurgeException( "Task not found: " + taskGuid );
        }

        List<GUID> taskGuids = this.collectTaskScope( taskGuid );
        Set<GUID> scope = new HashSet<>( taskGuids );

        TaskPurgeSafetyReport report = new TaskPurgeSafetyReport();
        report.setTaskGuid( taskGuid );
        report.setTaskGuids( taskGuids );
        report.setParentRefs( this.fetchExternalParentRefs( taskGuids, scope ) );
        report.setChildRefs( this.fetchExternalChildRefs( taskGuids, scope ) );
        List<TaskPurgeLineageRef> lineageRefs = new ArrayList<>();
        lineageRefs.addAll( report.getParentRefs() );
        lineageRefs.addAll( report.getChildRefs() );
        report.setLineageRefs( lineageRefs );
        report.setRunningInstances( this.fetchRunningInstances( taskGuids ) );
        report.setForceOfflineRequired( !report.getRunningInstances().isEmpty() );
        report.setHasWarnings( !report.getParentRefs().isEmpty() );
        report.setHasBlockingChildren( !report.getChildRefs().isEmpty() );
        report.setPurgeAllowed( !report.isHasBlockingChildren() && report.getRunningInstances().isEmpty() );
        report.setMessage( this.safetyMessage( report ) );
        return report;
    }

    @Override
    public TaskPurgeResult purge( TaskPurgeRequest request ) {
        TaskPurgeSafetyReport report = this.check( request );
        if ( report.isHasBlockingChildren() ) {
            throw new TaskPurgeException( "Task purge blocked by downstream lineage dependencies." );
        }
        if ( report.isForceOfflineRequired() && !request.isForceOffline() ) {
            throw new TaskPurgeException( "Task purge blocked by active task instances." );
        }

        TaskPurgeResult result = new TaskPurgeResult();
        result.setSafetyReport( report );
        if ( request.isForceOffline() ) {
            result.setSignaledProcessCount( this.signalRunningProcesses( report.getRunningInstances(), request, result ) );
        }

        List<GUID> taskGuids = report.getTaskGuids();
        List<GUID> instanceGuids = this.fetchInstanceGuids( taskGuids );
        this.primaryTask.getRavenTaskMasterManipulator().transaction().required( scope -> {
            this.purgeTaskData( scope, taskGuids, instanceGuids, result );
            return null;
        } );
        return result;
    }

    protected void purgeTaskData(
            OdinMappingTransactionScope scope, List<GUID> taskGuids, List<GUID> instanceGuids, TaskPurgeResult result
    ) {
        if ( instanceGuids != null && !instanceGuids.isEmpty() ) {
            result.setRemovedInstanceLineageEdgeCount(
                    scope.mapper( InstanceLineageAdjacentMapper.class ).deleteByInstanceGuids( instanceGuids )
            );
        }

        result.setRemovedExecAuditCount( scope.mapper( InstanceExecAuditMapper.class ).deleteByTaskGuids( taskGuids ) );
        result.setRemovedPatrolLogCount( scope.mapper( PatrolWatchdogLogMapper.class ).deleteByTaskGuids( taskGuids ) );
        result.setRemovedOperationLogCount( scope.mapper( TaskInstanceOperationLogMapper.class ).deleteByTaskGuids( taskGuids ) );
        result.setRemovedInstanceEventCount( scope.mapper( InstanceEventMapper.class ).deleteByTaskGuids( taskGuids ) );
        result.setRemovedExecCount( scope.mapper( InstanceExecMapper.class ).deleteByTaskGuids( taskGuids ) );
        result.setRemovedInstanceCount( scope.mapper( InstanceNodeMapper.class ).deleteByTaskGuids( taskGuids ) );
        result.setRemovedTaskLineageCount( scope.mapper( TaskLineageMapper.class ).deleteByTaskGuids( taskGuids ) );
        result.setRemovedTaskCount( this.removeTaskNodes( scope, taskGuids ) );
    }

    protected GUID requireTaskGuid( TaskPurgeRequest request ) {
        if ( request == null || request.getTaskGuid() == null ) {
            throw new TaskPurgeException( "Task purge request missing task guid." );
        }
        return request.getTaskGuid();
    }

    protected List<GUID> collectTaskScope( GUID taskGuid ) {
        LinkedHashSet<GUID> scope = new LinkedHashSet<>();
        this.collectTaskScope0( taskGuid, scope );
        return new ArrayList<>( scope );
    }

    protected void collectTaskScope0( GUID taskGuid, LinkedHashSet<GUID> scope ) {
        if ( taskGuid == null || scope.contains( taskGuid ) ) {
            return;
        }
        scope.add( taskGuid );
        Collection<TreeNode> children = this.primaryTask.getChildren( taskGuid );
        if ( children == null || children.isEmpty() ) {
            return;
        }
        for ( TreeNode child : children ) {
            if ( child == null || child.getGuid() == null ) {
                continue;
            }
            this.collectTaskScope0( child.getGuid(), scope );
        }
    }

    protected List<TaskPurgeLineageRef> fetchExternalParentRefs( List<GUID> taskGuids, Set<GUID> scope ) {
        List<TaskPurgeLineageRef> refs = new ArrayList<>();
        for ( GUID taskGuid : taskGuids ) {
            List<GUID> parents = this.atlasInstrument.fetchParentTaskGuids( taskGuid );
            refs.addAll( this.toExternalLineageRefs( taskGuid, parents, scope, LINEAGE_DIRECTION_PARENT ) );
        }
        return refs;
    }

    protected List<TaskPurgeLineageRef> fetchExternalChildRefs( List<GUID> taskGuids, Set<GUID> scope ) {
        List<TaskPurgeLineageRef> refs = new ArrayList<>();
        for ( GUID taskGuid : taskGuids ) {
            List<GUID> children = this.atlasInstrument.fetchChildTaskGuids( taskGuid );
            refs.addAll( this.toExternalLineageRefs( taskGuid, children, scope, LINEAGE_DIRECTION_CHILD ) );
        }
        return refs;
    }

    protected List<TaskPurgeLineageRef> toExternalLineageRefs(
            GUID taskGuid, List<GUID> relatedTaskGuids, Set<GUID> scope, String szDirection
    ) {
        List<TaskPurgeLineageRef> refs = new ArrayList<>();
        if ( relatedTaskGuids == null || relatedTaskGuids.isEmpty() ) {
            return refs;
        }
        for ( GUID relatedTaskGuid : relatedTaskGuids ) {
            if ( relatedTaskGuid == null || scope.contains( relatedTaskGuid ) ) {
                continue;
            }
            refs.add( this.toLineageRef( taskGuid, relatedTaskGuid, szDirection ) );
        }
        return refs;
    }

    protected TaskPurgeLineageRef toLineageRef( GUID taskGuid, GUID relatedTaskGuid, String szDirection ) {
        TaskPurgeLineageRef ref = new TaskPurgeLineageRef( taskGuid, relatedTaskGuid, szDirection );
        this.hydrateLineageTaskInfo( ref );
        return ref;
    }

    protected void hydrateLineageTaskInfo( TaskPurgeLineageRef ref ) {
        if ( ref == null ) {
            return;
        }
        TaskElement task = this.atlasInstrument.queryTaskElementByGuid( ref.getTaskGuid() );
        if ( task != null ) {
            ref.setTaskName( task.getName() );
            ref.setTaskPath( task.getSystemKernelObjectPath() );
        }

        TaskElement relatedTask = this.atlasInstrument.queryTaskElementByGuid( ref.getRelatedTaskGuid() );
        if ( relatedTask != null ) {
            ref.setRelatedTaskName( relatedTask.getName() );
            ref.setRelatedTaskPath( relatedTask.getSystemKernelObjectPath() );
        }
    }

    protected List<TaskPurgeRunningInstance> fetchRunningInstances( List<GUID> taskGuids ) {
        List<InstanceExec> execs = this.instanceExecMapper().fetchActiveByTaskGuids(
                taskGuids,
                List.of( TaskInstanceExecState.Submitted.getName(), TaskInstanceExecState.Running.getName() )
        );
        if ( execs == null || execs.isEmpty() ) {
            return new ArrayList<>();
        }

        List<TaskPurgeRunningInstance> runningInstances = new ArrayList<>();
        Set<GUID> deduplicatedInstances = new HashSet<>();
        for ( InstanceExec exec : execs ) {
            if ( exec == null || exec.getInstanceGuid() == null || deduplicatedInstances.contains( exec.getInstanceGuid() ) ) {
                continue;
            }
            InstanceEntry instance = this.primaryTask.getInstanceInstrument().getInstanceEntry( exec.getInstanceGuid() );
            if ( instance == null || !this.isActiveInstance( instance ) ) {
                continue;
            }
            deduplicatedInstances.add( exec.getInstanceGuid() );
            runningInstances.add( this.toRunningInstance( exec, instance ) );
        }
        return runningInstances;
    }

    protected TaskPurgeRunningInstance toRunningInstance( InstanceExec exec, InstanceEntry instance ) {
        TaskPurgeRunningInstance running = new TaskPurgeRunningInstance();
        running.setTaskGuid( exec.getTaskGuid() );
        running.setInstanceGuid( exec.getInstanceGuid() );
        running.setInstanceName( exec.getInstanceName() );
        running.setRunStatus( instance.getRunStatus() );
        running.setExecId( exec.getId() );
        running.setProcessGuid( exec.getProcessGuid() );
        running.setExecutedProcessor( exec.getExecutedProcessor() );
        running.setExecState( exec.getExecState() );
        running.setSequenceCnt( exec.getSequenceCnt() );
        running.setCurrentRetryNumber( exec.getCurrentRetryNumber() );
        return running;
    }

    protected boolean isActiveInstance( InstanceEntry instance ) {
        TaskInstanceStatus status = instance.getInstanceStatus();
        return status == TaskInstanceStatus.ProcessCreating
                || status == TaskInstanceStatus.ProcessStandby
                || status == TaskInstanceStatus.Running
                || status == TaskInstanceStatus.Audit;
    }

    protected int signalRunningProcesses(
            List<TaskPurgeRunningInstance> runningInstances, TaskPurgeRequest request, TaskPurgeResult result
    ) {
        int nSignaledCount = 0;
        for ( TaskPurgeRunningInstance runningInstance : runningInstances ) {
            if ( runningInstance.getProcessGuid() == null ) {
                result.getWarnings().add( "Active instance has no process guid: " + runningInstance.getInstanceGuid() );
                continue;
            }
            try {
                RemoteProcessSignalResult signalResult = this.remoteProcessManagerServer.signalRemoteUProcess(
                        runningInstance.getProcessGuid(),
                        ProcSignal.SIGKILL,
                        Math.max( 0L, request.getGraceTimeoutMillis() ),
                        this.signalReason( request )
                );
                if ( signalResult == null || !signalResult.isAccepted() ) {
                    result.getWarnings().add( "SIGKILL was not accepted for process: " + runningInstance.getProcessGuid() );
                }
                nSignaledCount++;
            }
            catch ( Exception e ) {
                throw new TaskPurgeException( e );
            }
        }
        return nSignaledCount;
    }

    protected String signalReason( TaskPurgeRequest request ) {
        if ( request == null || StringUtils.isBlank( request.getReason() ) ) {
            return "Task purge force offline";
        }
        return request.getReason();
    }

    protected List<GUID> fetchInstanceGuids( List<GUID> taskGuids ) {
        List<GUID> instanceGuids = new ArrayList<>();
        for ( GUID taskGuid : taskGuids ) {
            long nCount = this.primaryTask.getInstanceInstrument().countInstanceByGuid( taskGuid );
            if ( nCount <= 0 ) {
                continue;
            }
            List<InstanceEntry> instances = this.primaryTask.getInstanceInstrument().queryInstances( taskGuid, 0, nCount );
            if ( instances == null || instances.isEmpty() ) {
                continue;
            }
            for ( InstanceEntry instance : instances ) {
                if ( instance == null || instance.getGuid() == null ) {
                    continue;
                }
                instanceGuids.add( instance.getGuid() );
            }
        }
        return instanceGuids;
    }

    protected int removeTaskNodes( OdinMappingTransactionScope scope, List<GUID> taskGuids ) {
        int nRemovedCount = 0;
        TaskPathCacheMapper pathCacheMapper = scope.mapper( TaskPathCacheMapper.class );
        TaskNodeOwnerMapper ownerMapper = scope.mapper( TaskNodeOwnerMapper.class );
        TaskNodeMapper taskNodeMapper = scope.mapper( TaskNodeMapper.class );
        TaskNamespaceMapper namespaceMapper = scope.mapper( TaskNamespaceMapper.class );
        AppNodeMapper appNodeMapper = scope.mapper( AppNodeMapper.class );
        TaskTreeMapper taskTreeMapper = scope.mapper( TaskTreeMapper.class );

        for ( int i = taskGuids.size() - 1; i >= 0; i-- ) {
            GUID taskGuid = taskGuids.get( i );
            if ( taskGuid == null ) {
                continue;
            }
            TreeNode treeNode = this.primaryTask.get( taskGuid );
            pathCacheMapper.remove( taskGuid );
            ownerMapper.removeBySubordinate( taskGuid );
            if ( treeNode instanceof Namespace ) {
                namespaceMapper.remove( taskGuid );
            }
            else if ( treeNode instanceof AppElement ) {
                appNodeMapper.remove( taskGuid );
            }
            else {
                taskNodeMapper.remove( taskGuid );
            }
            taskTreeMapper.removeNodeRecord( taskGuid );
            nRemovedCount++;
        }
        return nRemovedCount;
    }

    protected String safetyMessage( TaskPurgeSafetyReport report ) {
        if ( report == null ) {
            return "";
        }
        if ( report.isHasBlockingChildren() ) {
            return "Task purge blocked by downstream lineage dependencies.";
        }
        if ( !report.getRunningInstances().isEmpty() ) {
            return "Task purge requires force offline for active task instances.";
        }
        if ( report.isHasWarnings() ) {
            return "Task purge safety check passed with upstream lineage warnings.";
        }
        return "Task purge safety check passed.";
    }

    protected ScheduleManipulator scheduleManipulator() {
        return this.primaryTask.getRavenTaskMasterManipulator().getScheduleManipulator();
    }

    protected InstanceExecMapper instanceExecMapper() {
        return this.scheduleManipulator().getInstanceExecMapper();
    }

    @SuppressWarnings( "unused" )
    protected InstanceLineageAdjacentMapper instanceLineageAdjacentMapper() {
        return this.scheduleManipulator().getInstanceLineageAdjacentMapper();
    }

    @SuppressWarnings( "unused" )
    protected InstanceExecAuditMapper instanceExecAuditMapper() {
        return this.scheduleManipulator().getInstanceExecAuditMapper();
    }

    @SuppressWarnings( "unused" )
    protected PatrolWatchdogLogMapper patrolWatchdogLogMapper() {
        return this.scheduleManipulator().getPatrolWatchdogLogMapper();
    }

    @SuppressWarnings( "unused" )
    protected TaskInstanceOperationLogMapper taskInstanceOperationLogMapper() {
        return this.scheduleManipulator().getTaskInstanceOperationLogMapper();
    }

    @SuppressWarnings( "unused" )
    protected InstanceEventMapper instanceEventMapper() {
        return this.scheduleManipulator().getInstanceEventMapper();
    }
}
