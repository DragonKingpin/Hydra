package com.walnut.odin.conduct.schedule;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousContext;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousPrepareResult;

public interface TaskInstantaneousPreparator extends Pinenut {

    UniformTaskScheduler taskScheduler();

    TaskInstantaneousPrepareResult prepare( TaskInstantaneousContext context ) throws MetaPersistenceException;

}
