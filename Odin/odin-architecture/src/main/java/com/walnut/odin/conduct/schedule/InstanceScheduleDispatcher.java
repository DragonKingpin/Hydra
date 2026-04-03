package com.walnut.odin.conduct.schedule;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.schedule.entity.ScheduleLaunchContext;

public interface InstanceScheduleDispatcher extends Pinenut {

    String getPartitionName();

    ScheduleLaunchContext pipeLaunch(Collection<InstanceEntry> instances );

}
