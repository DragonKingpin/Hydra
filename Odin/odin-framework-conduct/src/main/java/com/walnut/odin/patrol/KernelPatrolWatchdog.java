package com.walnut.odin.patrol;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.slime.meta.TableIndexMeta;
import com.walnut.odin.conduct.entity.GenericTaskInstanceOperationLog;
import com.walnut.odin.conduct.entity.TaskInstanceOperationActor;
import com.walnut.odin.conduct.entity.TaskInstanceOperationLog;
import com.walnut.odin.conduct.entity.TaskInstanceOperationSource;
import com.walnut.odin.conduct.entity.TaskInstanceOperationType;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleExaminer;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionReason;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionResult;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.dispatch.TaskExecutionProcessor;
import com.walnut.odin.dispatch.TaskLaunchContext;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.mapper.PatrolWatchdogLogMapper;
import com.walnut.odin.task.mapper.TaskInstanceOperationLogMapper;
import com.walnut.odin.task.source.ScheduleManipulator;

public class KernelPatrolWatchdog implements PatrolWatchdog {

    public static final String WatchdogName             = "KernelPatrolWatchdog";
    public static final String StartupRuleCode          = "WATCHDOG_SERVICE_START";
    public static final String StartupRuleName          = "PatrolWatchdog Service Start";
    public static final String RunningAliveRuleCode     = "RUNNING_PROCESS_ALIVE_BASELINE";
    public static final String RunningAliveRuleName     = "Running Process Alive Baseline";
    public static final String TargetTypeInstanceExec   = "InstanceExec";
    public static final String PatrolStatePass          = "Pass";
    public static final String PatrolStateFail          = "Fail";
    public static final String SeverityInfo             = "Info";
    public static final String SeverityError            = "Error";
    public static final String ActionTypeNone           = "None";
    public static final String ActionTypeMarkFail       = "MarkFail";
    public static final String ActionStateSuccess       = "Success";
    public static final String ActionStateFailed        = "Failed";

    protected Logger                           log = LoggerFactory.getLogger( this.getClass() );

    protected UniformTaskScheduler             mTaskScheduler;
    protected RavenTaskConfig                  mRavenTaskConfig;
    protected InstanceExecMapper               mInstanceExecMapper;
    protected PatrolWatchdogLogMapper          mPatrolWatchdogLogMapper;
    protected TaskInstanceOperationLogMapper   mTaskInstanceOperationLogMapper;
    protected GuidAllocator                    mGuidAllocator;
    protected TaskInstanceLifecycleExaminer mTaskInstanceLifecycleExaminer;
    protected ScheduledExecutorService         mExecutor;
    protected AtomicBoolean                    mServiceRunning;
    protected AtomicBoolean                    mEnabled;
    protected AtomicBoolean                    mRunningProcessAliveEnabled;
    protected AtomicBoolean                    mPatrolling;
    protected long                             mnStartupTimeMillis;
    protected volatile LocalDateTime           mLastPatrolStartTime;
    protected volatile LocalDateTime           mLastPatrolFinishTime;
    protected volatile int                     mnLastScannedCount;
    protected volatile int                     mnLastFailedCount;

    public KernelPatrolWatchdog( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler       = taskScheduler;
        this.mRavenTaskConfig     = taskScheduler.ravenTaskConfig();

        ScheduleManipulator scheduleManipulator = taskScheduler.taskInstrument().getRavenTaskMasterManipulator().getScheduleManipulator();
        this.mInstanceExecMapper      = scheduleManipulator.getInstanceExecMapper();
        this.mPatrolWatchdogLogMapper = scheduleManipulator.getPatrolWatchdogLogMapper();
        this.mTaskInstanceOperationLogMapper = scheduleManipulator.getTaskInstanceOperationLogMapper();
        this.mGuidAllocator           = taskScheduler.instanceInstrument().getTaskInstrument().getGuidAllocator();
        this.mTaskInstanceLifecycleExaminer = taskScheduler.taskInstanceLifecycleExaminer();
        this.mServiceRunning             = new AtomicBoolean( false );
        this.mEnabled                    = new AtomicBoolean( this.mRavenTaskConfig.isPatrolWatchdogEnabled() );
        this.mRunningProcessAliveEnabled = new AtomicBoolean( this.mRavenTaskConfig.isPatrolWatchdogRunningProcessAliveEnabled() );
        this.mPatrolling                 = new AtomicBoolean( false );
    }

    @Override
    public void startService() {
        if ( !this.mRavenTaskConfig.isPatrolWatchdogEnabled() ) {
            this.log.info( "[PatrolWatchdog] [Disabled] (Reason: `config-disabled`) <Pass>" );
            return;
        }
        if ( !this.mServiceRunning.compareAndSet( false, true ) ) {
            return;
        }

        this.mnStartupTimeMillis = System.currentTimeMillis();
        this.mEnabled.set( true );
        this.recordServiceStarted();

        long nPulseMillis = Math.max( 1L, this.mRavenTaskConfig.getPatrolWatchdogPulseMillis() );
        this.mExecutor = Executors.newSingleThreadScheduledExecutor( runnable -> {
            Thread thread = new Thread( runnable, "odin-patrol-watchdog" );
            thread.setDaemon( true );
            return thread;
        } );
        this.mExecutor.scheduleWithFixedDelay(
                this::safePatrolOnce,
                nPulseMillis,
                nPulseMillis,
                TimeUnit.MILLISECONDS
        );
        this.log.info(
                "[PatrolWatchdog] [Started] (PulseMillis: `{}`, StartupObservationMillis: `{}`, RunningLostGraceMillis: `{}`) <Ready>",
                nPulseMillis,
                this.mRavenTaskConfig.getPatrolWatchdogStartupObservationMillis(),
                this.mRavenTaskConfig.getPatrolWatchdogRunningLostGraceMillis()
        );
    }

    @Override
    public void terminateService() {
        if ( !this.mServiceRunning.compareAndSet( true, false ) ) {
            return;
        }

        ScheduledExecutorService executor = this.mExecutor;
        this.mExecutor = null;
        if ( executor != null ) {
            executor.shutdownNow();
        }
        this.log.info( "[PatrolWatchdog] [Stopped] <Done>" );
    }

    @Override
    public boolean isEnabled() {
        return this.mEnabled.get();
    }

    @Override
    public void enable() {
        this.mEnabled.set( true );
        this.log.info( "[PatrolWatchdog] [Enabled] <Done>" );
    }

    @Override
    public void disable() {
        this.mEnabled.set( false );
        this.log.info( "[PatrolWatchdog] [Disabled] (Reason: `runtime-disabled`) <Done>" );
    }

    @Override
    public void enableRule( String ruleCode ) {
        if ( !RunningAliveRuleCode.equals( ruleCode ) ) {
            throw new IllegalArgumentException( "Unsupported patrol watchdog rule: " + ruleCode );
        }
        this.mRunningProcessAliveEnabled.set( true );
        this.log.info( "[PatrolWatchdog] [RuleEnabled] (RuleCode: `{}`) <Done>", ruleCode );
    }

    @Override
    public void disableRule( String ruleCode ) {
        if ( !RunningAliveRuleCode.equals( ruleCode ) ) {
            throw new IllegalArgumentException( "Unsupported patrol watchdog rule: " + ruleCode );
        }
        this.mRunningProcessAliveEnabled.set( false );
        this.log.info( "[PatrolWatchdog] [RuleDisabled] (RuleCode: `{}`) <Done>", ruleCode );
    }

    @Override
    public PatrolWatchdogSnapshot snapshot() {
        PatrolWatchdogSnapshot snapshot = new PatrolWatchdogSnapshot();
        snapshot.setWatchdogName( WatchdogName );
        snapshot.setServiceRunning( this.mServiceRunning.get() );
        snapshot.setEnabled( this.mEnabled.get() );
        snapshot.setPatrolling( this.mPatrolling.get() );
        snapshot.setPulseMillis( this.mRavenTaskConfig.getPatrolWatchdogPulseMillis() );
        snapshot.setStartupObservationMillis( this.mRavenTaskConfig.getPatrolWatchdogStartupObservationMillis() );
        snapshot.setRunningLostGraceMillis( this.mRavenTaskConfig.getPatrolWatchdogRunningLostGraceMillis() );
        snapshot.setScanIdWindow( this.mRavenTaskConfig.getPatrolWatchdogScanIdWindow() );
        if ( this.mnStartupTimeMillis > 0L ) {
            snapshot.setStartupTime( LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli( this.mnStartupTimeMillis ),
                    ZoneId.systemDefault()
            ) );
        }
        snapshot.setLastPatrolStartTime( this.mLastPatrolStartTime );
        snapshot.setLastPatrolFinishTime( this.mLastPatrolFinishTime );
        snapshot.setLastScannedCount( this.mnLastScannedCount );
        snapshot.setLastFailedCount( this.mnLastFailedCount );
        snapshot.setRules( List.of( this.runningProcessAliveRuleSnapshot() ) );
        return snapshot;
    }

    protected PatrolWatchdogRuleSnapshot runningProcessAliveRuleSnapshot() {
        PatrolWatchdogRuleSnapshot rule = new PatrolWatchdogRuleSnapshot();
        rule.setRuleCode( RunningAliveRuleCode );
        rule.setRuleName( RunningAliveRuleName );
        rule.setTargetType( TargetTypeInstanceExec );
        rule.setSystemRule( true );
        rule.setEnabled( this.mRunningProcessAliveEnabled.get() );
        rule.setDescription( "Checks Running instance exec records and marks zombie tasks failed when no live process evidence exists." );
        return rule;
    }

    @Override
    public void patrolOnce() {
        if ( !this.isEnabled() ) {
            return;
        }
        if ( !this.mRunningProcessAliveEnabled.get() ) {
            return;
        }
        if ( !this.mPatrolling.compareAndSet( false, true ) ) {
            return;
        }

        int nScanned = 0;
        int nFailed = 0;
        try {
            this.mLastPatrolStartTime = LocalDateTime.now();
            TableIndexMeta range = this.mInstanceExecMapper.selectRunningExecIdRange();
            if ( range == null || range.getMinId() <= 0 || range.getMaxId() <= 0 || range.getMaxId() < range.getMinId() ) {
                return;
            }

            long nWindow = Math.max( 1L, this.mRavenTaskConfig.getPatrolWatchdogScanIdWindow() );
            long nCursor = range.getMinId();
            while ( nCursor <= range.getMaxId() ) {
                long nWindowEnd = Math.min( nCursor + nWindow - 1L, range.getMaxId() );
                List<RunningExecPatrolEntry> entries = this.mInstanceExecMapper.fetchRunningExecPatrolEntries( nCursor, nWindowEnd );
                if ( entries != null && !entries.isEmpty() ) {
                    for ( RunningExecPatrolEntry entry : entries ) {
                        nScanned++;
                        if ( this.inspectRunningExec( entry ) ) {
                            nFailed++;
                        }
                    }
                }
                nCursor = nWindowEnd + 1L;
            }
        }
        finally {
            this.mLastPatrolFinishTime = LocalDateTime.now();
            this.mnLastScannedCount = nScanned;
            this.mnLastFailedCount = nFailed;
            this.mPatrolling.set( false );
            this.log.info( "[PatrolWatchdog] [Pulse] (Scanned: `{}`, Failed: `{}`) <Done>", nScanned, nFailed );
        }
    }

    protected void safePatrolOnce() {
        try {
            this.patrolOnce();
        }
        catch ( RuntimeException e ) {
            this.log.warn( "[PatrolWatchdog] [PulseFailure] <Ignored>", e );
        }
    }

    protected boolean inspectRunningExec( RunningExecPatrolEntry entry ) {
        if ( entry == null ) {
            return false;
        }
        if ( !this.isCurrentRunningInstanceExec( entry ) ) {
            return false;
        }
        if ( this.hasLiveProcessEvidence( entry ) ) {
            return false;
        }
        if ( this.isInStartupObservationWindow() ) {
            return false;
        }
        if ( !this.isRunningLostGraceExpired( entry ) ) {
            return false;
        }
        return this.markZombieRunningExecFailed( entry );
    }

    protected boolean isCurrentRunningInstanceExec( RunningExecPatrolEntry entry ) {
        if ( !TaskInstanceExecState.Running.getName().equals( entry.getExecState() ) ) {
            return false;
        }
        if ( !TaskInstanceStatus.Running.getName().equals( entry.getInstanceStatus() ) ) {
            return false;
        }
        if ( entry.getSequenceCnt() != entry.getInstanceSequenceCnt() ) {
            return false;
        }
        if ( entry.getCurrentRetryNumber() != entry.getInstanceRetryCnt() ) {
            return false;
        }
        Integer dryRun = entry.getDryRun();
        if ( dryRun != null && dryRun.intValue() != 0 ) {
            return false;
        }
        return true;
    }

    protected boolean hasLiveProcessEvidence( RunningExecPatrolEntry entry ) {
        if ( this.hasLiveLaunchContext( entry ) ) {
            return true;
        }
        return false;
    }

    protected boolean hasLiveLaunchContext( RunningExecPatrolEntry entry ) {
        GUID processGuid = entry.getProcessGuid();
        for ( TaskExecutionProcessor processor : this.mTaskScheduler.taskDispatcher().fetchProcessors() ) {
            Collection<TaskLaunchContext> contexts = this.mTaskScheduler.taskDispatcher().queryAffinityTasks( processor.getName() );
            if ( contexts == null || contexts.isEmpty() ) {
                continue;
            }
            for ( TaskLaunchContext context : contexts ) {
                if ( !this.matchesEntryContext( entry, processGuid, context ) ) {
                    continue;
                }
                if ( this.isLiveProcess( context.getLaunchedProcess() ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean matchesEntryContext( RunningExecPatrolEntry entry, GUID processGuid, TaskLaunchContext context ) {
        if ( context == null || context.getTaskInstance() == null || context.getTaskInstance().getInstanceEntry() == null ) {
            return false;
        }
        if ( !entry.getInstanceGuid().equals( context.getTaskInstance().getInstanceEntry().getGuid() ) ) {
            return false;
        }
        if ( context.getTaskInstance().getInstanceEntry().getSequenceCnt() != entry.getSequenceCnt() ) {
            return false;
        }
        if ( context.getTaskInstance().getInstanceEntry().getRetryCnt() != entry.getCurrentRetryNumber() ) {
            return false;
        }
        UProcess process = context.getLaunchedProcess();
        if ( processGuid == null ) {
            return true;
        }
        if ( process == null || process.getPID() == null ) {
            return false;
        }
        return processGuid.equals( process.getPID() );
    }

    protected boolean isLiveProcess( UProcess process ) {
        if ( process == null ) {
            return false;
        }
        UProcessStatus status = process.getStatus();
        if ( status == null ) {
            return true;
        }
        return !status.isTerminal();
    }

    protected boolean isInStartupObservationWindow() {
        long nObservationMillis = Math.max( 0L, this.mRavenTaskConfig.getPatrolWatchdogStartupObservationMillis() );
        return System.currentTimeMillis() - this.mnStartupTimeMillis < nObservationMillis;
    }

    protected boolean isRunningLostGraceExpired( RunningExecPatrolEntry entry ) {
        LocalDateTime baseTime = this.resolveRunningLostBaseTime( entry );
        if ( baseTime == null ) {
            return false;
        }
        long nGraceMillis = Math.max( 0L, this.mRavenTaskConfig.getPatrolWatchdogRunningLostGraceMillis() );
        return Duration.between( baseTime, LocalDateTime.now() ).toMillis() >= nGraceMillis;
    }

    protected LocalDateTime resolveRunningLostBaseTime( RunningExecPatrolEntry entry ) {
        LocalDateTime baseTime = entry.getExecRunTime();
        baseTime = this.maxTime( baseTime, entry.getExecStartTime() );
        baseTime = this.maxTime( baseTime, entry.getExecUpdateTime() );
        baseTime = this.maxTime( baseTime, entry.getInstanceUpdateTime() );
        return baseTime;
    }

    protected LocalDateTime maxTime( LocalDateTime left, LocalDateTime right ) {
        if ( left == null ) {
            return right;
        }
        if ( right == null ) {
            return left;
        }
        if ( right.isAfter( left ) ) {
            return right;
        }
        return left;
    }

    protected boolean markZombieRunningExecFailed( RunningExecPatrolEntry entry ) {
        LocalDateTime now = LocalDateTime.now();
        String szCause = "PatrolWatchdog: running exec lost live process.";
        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleExaminer.transitCurrentRetryWithRuntimeFields(
                entry.getInstanceGuid(),
                entry.getSequenceCnt(),
                entry.getCurrentRetryNumber(),
                List.of( TaskInstanceStatus.Running ),
                TaskInstanceStatus.Error,
                TaskInstanceTransitionReason.ProcessFailed,
                null,
                now,
                now,
                szCause
        );
        if ( !result.isSucceeded() ) {
            this.recordRunningAliveLog( entry, PatrolStateFail, SeverityError, ActionTypeMarkFail, ActionStateFailed, now, "Zombie running exec detected, but instance transition failed." );
            return false;
        }

        this.mTaskScheduler.instanceScheduleAllocator().reclaimInstance( entry.getInstanceGuid() );
        int nAffectedRows = this.mInstanceExecMapper.updateStateRetryMonotonic(
                entry.getInstanceGuid(),
                entry.getSequenceCnt(),
                entry.getCurrentRetryNumber(),
                TaskInstanceExecState.Fail.getName(),
                null,
                null,
                now
        );
        if ( nAffectedRows <= 0 ) {
            this.recordRunningAliveLog( entry, PatrolStateFail, SeverityError, ActionTypeMarkFail, ActionStateFailed, now, "Zombie running instance marked Error, but exec state update missed." );
            return false;
        }

        this.recordRunningAliveLog( entry, PatrolStateFail, SeverityError, ActionTypeMarkFail, ActionStateSuccess, now, szCause );
        this.recordSystemOperationLog( entry, TaskInstanceOperationType.SystemMarkFail, "Zombie running instance marked Error", now );
        this.log.warn(
                "[PatrolWatchdog] [ZombieRunningExec] (InstanceGuid: `{}`, ExecId: `{}`, ProcessGuid: `{}`, Processor: `{}`) <MarkedFail>",
                entry.getInstanceGuid(),
                entry.getExecId(),
                entry.getProcessGuid(),
                entry.getExecutedProcessor()
        );
        return true;
    }

    protected void recordServiceStarted() {
        GenericPatrolWatchdogLog record = this.baseLog( StartupRuleCode, StartupRuleName, LocalDateTime.now() );
        record.setTargetType( "PatrolWatchdog" );
        record.setPatrolState( PatrolStatePass );
        record.setSeverity( SeverityInfo );
        record.setActionType( ActionTypeNone );
        record.setActionState( ActionStateSuccess );
        record.setMessage( "PatrolWatchdog service started." );
        this.insertLogQuietly( record );
    }

    protected void recordRunningAliveLog(
            RunningExecPatrolEntry entry, String szPatrolState, String szSeverity, String szActionType,
            String szActionState, LocalDateTime actionTime, String szMessage
    ) {
        GenericPatrolWatchdogLog record = this.baseLog( RunningAliveRuleCode, RunningAliveRuleName, LocalDateTime.now() );
        record.setTargetType( TargetTypeInstanceExec );
        record.setTaskGuid( entry.getTaskGuid() );
        record.setInstanceGuid( entry.getInstanceGuid() );
        record.setTargetGuid( entry.getInstanceGuid() );
        record.setExecId( entry.getExecId() );
        record.setProcessGuid( entry.getProcessGuid() );
        record.setExecutedProcessor( entry.getExecutedProcessor() );
        record.setPatrolState( szPatrolState );
        record.setSeverity( szSeverity );
        record.setActionType( szActionType );
        record.setActionState( szActionState );
        record.setMessage( szMessage );
        record.setActionTime( actionTime );
        record.setPayload( this.runningAlivePayload( entry ) );
        this.insertLogQuietly( record );
    }

    protected GenericPatrolWatchdogLog baseLog( String szRuleCode, String szRuleName, LocalDateTime patrolTime ) {
        GenericPatrolWatchdogLog record = new GenericPatrolWatchdogLog();
        record.setGuid( this.mGuidAllocator.nextGUID() );
        record.setWatchdogName( WatchdogName );
        record.setRuleCode( szRuleCode );
        record.setRuleName( szRuleName );
        record.setPatrolTime( patrolTime );
        return record;
    }

    protected String runningAlivePayload( RunningExecPatrolEntry entry ) {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "execState"         , entry.getExecState()           ),
                new KeyValue<>( "instanceStatus"    , entry.getInstanceStatus()      ),
                new KeyValue<>( "sequenceCnt"       , entry.getSequenceCnt()         ),
                new KeyValue<>( "currentRetryNumber", entry.getCurrentRetryNumber()  ),
                new KeyValue<>( "processGuid"       , entry.getProcessGuid()         ),
                new KeyValue<>( "executedProcessor" , entry.getExecutedProcessor()   )
        } );
    }

    protected void recordSystemOperationLog(
            RunningExecPatrolEntry entry, TaskInstanceOperationType operationType, String szMessage, LocalDateTime operationTime
    ) {
        GenericTaskInstanceOperationLog record = new GenericTaskInstanceOperationLog();
        record.setGuid( this.mGuidAllocator.nextGUID() );
        record.setTaskGuid( entry.getTaskGuid() );
        record.setInstanceGuid( entry.getInstanceGuid() );
        record.setInstanceName( entry.getInstanceName() );
        record.setOperationType( operationType.name() );
        record.setOperationSource( TaskInstanceOperationSource.System.name() );
        record.setUserIdentifier( TaskInstanceOperationActor.System.getUserIdentifier() );
        record.setUserName( TaskInstanceOperationActor.System.getUserName() );
        record.setMessage( szMessage );
        record.setPayload( this.systemMarkFailPayload( entry ) );
        record.setOperationTime( operationTime );
        this.insertOperationLogQuietly( record );
    }

    protected String systemMarkFailPayload( RunningExecPatrolEntry entry ) {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "ruleCode"          , RunningAliveRuleCode          ),
                new KeyValue<>( "actionType"        , ActionTypeMarkFail            ),
                new KeyValue<>( "actionState"       , ActionStateSuccess            ),
                new KeyValue<>( "execId"            , entry.getExecId()             ),
                new KeyValue<>( "execState"         , entry.getExecState()          ),
                new KeyValue<>( "instanceStatus"    , entry.getInstanceStatus()     ),
                new KeyValue<>( "sequenceCnt"       , entry.getSequenceCnt()        ),
                new KeyValue<>( "currentRetryNumber", entry.getCurrentRetryNumber() ),
                new KeyValue<>( "processGuid"       , entry.getProcessGuid()        ),
                new KeyValue<>( "executedProcessor" , entry.getExecutedProcessor()  )
        } );
    }

    protected void insertLogQuietly( GenericPatrolWatchdogLog record ) {
        if ( this.mPatrolWatchdogLogMapper == null ) {
            return;
        }
        try {
            this.mPatrolWatchdogLogMapper.insert( record );
        }
        catch ( RuntimeException e ) {
            this.log.warn( "[PatrolWatchdog] [LogInsertFailure] (RuleCode: `{}`) <Ignored>", record.getRuleCode(), e );
        }
    }

    protected void insertOperationLogQuietly( TaskInstanceOperationLog record ) {
        if ( this.mTaskInstanceOperationLogMapper == null ) {
            return;
        }
        try {
            this.mTaskInstanceOperationLogMapper.insert( record );
        }
        catch ( RuntimeException e ) {
            this.log.warn(
                    "[PatrolWatchdog] [OperationLogInsertFailure] (InstanceGuid: `{}`, OperationType: `{}`) <Ignored>",
                    record.getInstanceGuid(),
                    record.getOperationType(),
                    e
            );
        }
    }
}
