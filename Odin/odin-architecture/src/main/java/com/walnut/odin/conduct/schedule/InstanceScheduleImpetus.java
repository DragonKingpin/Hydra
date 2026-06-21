package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.TaskInstanceStatus;

public interface InstanceScheduleImpetus extends Pinenut {

    UniformTaskScheduler taskScheduler();

    void impelSchedulableInstances( Collection<TaskInstanceStatus> statuses, LocalDateTime targetTime );

    void impelPrelaunchInstances( LocalDateTime targetTime );

    void impelPreparedStandbyInstances( LocalDateTime targetTime );

}
