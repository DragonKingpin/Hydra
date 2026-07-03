package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.schedule.entity.InstanceDepartureResult;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;

public interface InstanceInstantaneousImpetus extends Pinenut {

    UniformTaskScheduler taskScheduler();

    InstanceDepartureResult impel(
            Collection<InstanceEntry> instances, LocalDateTime scheduleTime, LaunchFeature launchFeature
    ) throws InstanceLaunchException, TaskDispatchException;

}
