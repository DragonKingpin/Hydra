package com.walnut.odin.conduct.schedule;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.schedule.entity.ScheduleFittingContext;

public interface InstanceScheduleAllocator extends Pinenut {

    String getPartitionName();

    ScheduleFittingContext pipeFitting( Collection<InstanceEntry> instances );

}
