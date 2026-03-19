package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;

public interface UniformTaskScheduler extends Pinenut {

    List<TaskElement> fetchSchedulableTasksInRange( long idMin, long idMax, Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime );

    List<TaskElement> fetchSchedulableTasksDaily( long idMin, long idMax, LocalDateTime targetTime );

}
