package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;

import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;

import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.troll.TaskExecutionElevator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RavenTaskScheduler implements UniformTaskScheduler {

    private RavenTaskConfig            mRavenTaskConfig;

    private InstanceInstrument         mInstanceInstrument;
    private UniformTaskInstrument      mUniformTaskInstrument;
    private RuntimeAtlasInstrument     mRuntimeAtlasInstrument;
    private CentralizedTaskInstrument  mCentralizedTaskInstrument;
    private TaskExecutionElevator      mTaskExecutionElevator;

    private TaskSchedulePreparator     mTaskSchedulePreparator;
    private InstanceScheduleImpetus    mInstanceScheduleImpetus;


    public RavenTaskScheduler(
            CentralizedTaskInstrument taskInstrument, RuntimeAtlasInstrument atlasInstrument, TaskExecutionElevator elevator
    ) {
        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskScheduler Construction) <Start>" );

        this.mCentralizedTaskInstrument  = taskInstrument;
        this.mUniformTaskInstrument      = taskInstrument.getUniformTaskInstrument();
        this.mInstanceInstrument         = this.mUniformTaskInstrument.getInstanceInstrument();
        this.mRuntimeAtlasInstrument     = atlasInstrument;
        this.mTaskExecutionElevator      = elevator;

        this.mRavenTaskConfig            = (RavenTaskConfig) taskInstrument.getConfig();

        this.mTaskSchedulePreparator     = new RavenTaskSchedulePreparator( this );
        this.mInstanceScheduleImpetus    = new RavenInstanceScheduleImpetus( this );

        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskScheduler Construction) <Done>" );
    }


    @Override
    public TaskSchedulePreparator taskSchedulePreparator() {
        return this.mTaskSchedulePreparator;
    }

    @Override
    public InstanceScheduleImpetus instanceScheduleLauncher() {
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
    public TaskExecutionElevator taskExecutionElevator() {
        return this.mTaskExecutionElevator;
    }


    public void fetch() {
        //this.mTaskSchedulePreparator.prepareSchedulableTasksDaily( LocalDateTime.now() );
        this.mInstanceScheduleImpetus.impelSchedulableInstances( TaskInstanceStatus.New, LocalDateTime.now() );
    }


}
