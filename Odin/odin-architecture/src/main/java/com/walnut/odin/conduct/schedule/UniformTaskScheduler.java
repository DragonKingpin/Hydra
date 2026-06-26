package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleExaminer;
import com.walnut.odin.conduct.schedule.entity.TaskSchedulerRuntimeSnapshot;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitRequest;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitResult;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.patrol.PatrolWatchdog;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.launch.TaskLaunchFeatureProviderRegistry;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

public interface UniformTaskScheduler extends Pinenut {

    RavenTaskConfig ravenTaskConfig();

    CentralizedTaskInstrument taskInstrument();

    InstanceInstrument instanceInstrument();

    RuntimeAtlasInstrument atlasInstrument();

    TaskExecutionLauncher taskExecutionLauncher();

    TaskInstanceLifecycleExaminer taskInstanceLifecycleExaminer();

    TaskDispatcher taskDispatcher();

    TaskLaunchFeatureProviderRegistry taskLaunchFeatureProviderRegistry();

    String getPartitionName();

    void startService();

    void terminateService();

    boolean isRunning();

    TaskSchedulerRuntimeSnapshot runtimeSnapshot();

    void startCycleEngine();

    void stopCycleEngine();

    void pulseSchedule();

    void pulseSchedule( LocalDateTime pulseTime );

    void pulseScheduleDaily( LocalDateTime pulseTime );

    InstantaneousEngine instantaneousEngine();

    TaskInstantaneousSubmitResult submitInstantaneousTask( TaskInstantaneousSubmitRequest request )
            throws MetaPersistenceException, InstanceLaunchException, TaskDispatchException;

    TaskSchedulePreparator taskSchedulePreparator();

    TaskInstantaneousPreparator taskInstantaneousPreparator();

    InstanceScheduleImpetus instanceScheduleImpetus();

    InstanceInstantaneousImpetus instanceInstantaneousImpetus();

    InstanceDepartureGate instanceDepartureGate();

    InstanceScheduleAllocator instanceScheduleAllocator();

    PatrolWatchdog patrolWatchdog();

}
