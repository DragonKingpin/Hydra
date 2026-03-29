package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.TaskInstanceStatus;

public interface InstanceScheduleImpetus extends Pinenut {

    UniformTaskScheduler taskScheduler();

    void impelSchedulableInstances( TaskInstanceStatus status, LocalDateTime targetTime );

}
