package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;

import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;

import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RavenTaskScheduler implements UniformTaskScheduler {

    private RavenTaskConfig              mRavenTaskConfig;

    private InstanceInstrument           mInstanceInstrument;
    private UniformTaskInstrument        mUniformTaskInstrument;
    private RuntimeAtlasInstrument       mRuntimeAtlasInstrument;
    private CentralizedTaskInstrument    mCentralizedTaskInstrument;
    private TaskExecutionLauncher        mTaskExecutionLauncher;

    private TaskSchedulePreparator       mTaskSchedulePreparator;
    private InstanceScheduleImpetus      mInstanceScheduleImpetus;

    private InstanceScheduleDispatcher   mInstanceScheduleDispatcher;
    private String                       mszPartitionName;

    public RavenTaskScheduler(
            CentralizedTaskInstrument taskInstrument, RuntimeAtlasInstrument atlasInstrument, TaskExecutionLauncher launcher
    ) {
        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskScheduler Construction) <Start>" );

        this.mCentralizedTaskInstrument  = taskInstrument;
        this.mUniformTaskInstrument      = taskInstrument.getUniformTaskInstrument();
        this.mInstanceInstrument         = this.mUniformTaskInstrument.getInstanceInstrument();
        this.mRuntimeAtlasInstrument     = atlasInstrument;
        this.mTaskExecutionLauncher      = launcher;

        this.mRavenTaskConfig            = (RavenTaskConfig) taskInstrument.getConfig();
        this.mszPartitionName            = this.mRavenTaskConfig.getSchedulePartitionName();

        this.mTaskSchedulePreparator     = new RavenTaskSchedulePreparator( this );
        this.mInstanceScheduleImpetus    = new RavenInstanceScheduleImpetus( this );
        this.mInstanceScheduleDispatcher = new RavenScheduleDispatcher( this );

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
    public String getPartitionName() {
        return this.mszPartitionName;
    }

    public void fetch() {
        //this.mTaskSchedulePreparator.prepareSchedulableTasksDaily( LocalDateTime.now() );
        this.mInstanceScheduleImpetus.impelSchedulableInstances( TaskInstanceStatus.New, LocalDateTime.now() );
    }


}
