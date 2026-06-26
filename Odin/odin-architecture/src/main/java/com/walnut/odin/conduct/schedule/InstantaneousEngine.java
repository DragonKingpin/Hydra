package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.conduct.schedule.entity.TaskSchedulerEngineRuntimeSnapshot;

public interface InstantaneousEngine extends Pinenut {

    UniformTaskScheduler taskScheduler();

    void startService();

    void terminateService( long gracefulShutdownMillis );

    void pulse();

    void pulse( LocalDateTime pulseTime );

    boolean isRunning();

    TaskSchedulerEngineRuntimeSnapshot runtimeSnapshot();
}
