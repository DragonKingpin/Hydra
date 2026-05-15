package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.List;

import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;

import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.recovery.KernelTaskSchedulerReconciler;
import com.walnut.odin.conduct.recovery.TaskSchedulerReconciler;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RavenTaskScheduler implements UniformTaskScheduler {

    private static final Logger          log = LoggerFactory.getLogger( RavenTaskScheduler.class );

    private RavenTaskConfig              mRavenTaskConfig;

    private InstanceInstrument           mInstanceInstrument;
    private UniformTaskInstrument        mUniformTaskInstrument;
    private RuntimeAtlasInstrument       mRuntimeAtlasInstrument;
    private CentralizedTaskInstrument    mCentralizedTaskInstrument;

    private TaskExecutionLauncher        mTaskExecutionLauncher;
    private TaskDispatcher               mTaskDispatcher;

    private TaskSchedulePreparator       mTaskSchedulePreparator;
    private InstanceScheduleImpetus      mInstanceScheduleImpetus;

    private InstanceScheduleAllocator    mInstanceScheduleAllocator;
    private TaskSchedulerReconciler      mTaskSchedulerReconciler;
    private String                       mszPartitionName;

    public RavenTaskScheduler(
            CentralizedTaskInstrument taskInstrument, RuntimeAtlasInstrument atlasInstrument,
            TaskDispatcher dispatcher
    ) {
        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskScheduler Construction) <Start>" );

        this.mCentralizedTaskInstrument  = taskInstrument;
        this.mUniformTaskInstrument      = taskInstrument.getUniformTaskInstrument();
        this.mInstanceInstrument         = this.mUniformTaskInstrument.getInstanceInstrument();
        this.mRuntimeAtlasInstrument     = atlasInstrument;

        this.mTaskExecutionLauncher      = dispatcher.taskExecutionLauncher();
        this.mTaskDispatcher             = dispatcher;

        this.mRavenTaskConfig            = (RavenTaskConfig) taskInstrument.getConfig();
        this.mszPartitionName            = this.mRavenTaskConfig.getSchedulePartitionName();

        this.mInstanceScheduleAllocator  = new RavenScheduleAllocator( this ); // [1]
        this.mTaskSchedulePreparator     = new RavenTaskSchedulePreparator( this ); // [2]
        this.mInstanceScheduleImpetus    = new RavenInstanceScheduleImpetus( this ); // [3]
        this.mTaskSchedulerReconciler    = new KernelTaskSchedulerReconciler( this ); // [4]


        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskScheduler Construction) <Done>" );
    }


    @Override
    public TaskSchedulePreparator taskSchedulePreparator() {
        return this.mTaskSchedulePreparator;
    }

    @Override
    public InstanceScheduleImpetus instanceScheduleImpetus() {
        return this.mInstanceScheduleImpetus;
    }

    @Override
    public InstanceScheduleAllocator instanceScheduleAllocator() {
        return this.mInstanceScheduleAllocator;
    }

    @Override
    public RavenTaskConfig ravenTaskConfig() {
        return this.mRavenTaskConfig;
    }

    @Override
    public CentralizedTaskInstrument taskInstrument() {
        return this.mCentralizedTaskInstrument;
    }

    @Override
    public InstanceInstrument instanceInstrument() {
        return this.mInstanceInstrument;
    }

    @Override
    public RuntimeAtlasInstrument atlasInstrument() {
        return this.mRuntimeAtlasInstrument;
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
    public String getPartitionName() {
        return this.mszPartitionName;
    }

    @Override
    public void pulseSchedule() {
        this.pulseSchedule( LocalDateTime.now() );
    }

    @Override
    public void pulseSchedule( LocalDateTime pulseTime ) {
        if ( pulseTime == null ) {
            pulseTime = LocalDateTime.now();
        }

        this.mTaskSchedulerReconciler.reconcileLightweight( pulseTime );
        this.mTaskSchedulePreparator.prepareHourlySchedulableTasksAndWait( pulseTime );
        this.mTaskSchedulePreparator.prepareFastSchedulableTasksAndWait( pulseTime );
        this.mInstanceScheduleImpetus.impelPrelaunchInstances( pulseTime );
    }

    @Override
    public void pulseScheduleDaily( LocalDateTime pulseTime ) {
        if ( pulseTime == null ) {
            pulseTime = LocalDateTime.now();
        }

        this.mTaskSchedulePreparator.prepareDailySchedulableTasksAndWait( pulseTime );
    }


}
