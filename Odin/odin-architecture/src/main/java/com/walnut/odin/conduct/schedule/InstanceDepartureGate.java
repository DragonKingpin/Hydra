package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.schedule.entity.InstanceDepartureResult;

public interface InstanceDepartureGate extends Pinenut {

    UniformTaskScheduler taskScheduler();

    InstanceDepartureResult prepareDeparture(
            Collection<InstanceEntry> instances, LocalDateTime scheduleTime
    ) throws MetaPersistenceException;

}
