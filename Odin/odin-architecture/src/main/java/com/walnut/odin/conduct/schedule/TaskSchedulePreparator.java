package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;

public interface TaskSchedulePreparator extends Pinenut {

    UniformTaskScheduler taskScheduler();


    void prepareSchedulableTasksAndWait( Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime );

    void prepareDailySchedulableTasksAndWait( LocalDateTime targetTime );

    void prepareHourlySchedulableTasksAndWait( LocalDateTime targetTime );

    void prepareFastSchedulableTasksAndWait( LocalDateTime targetTime );

    List<TaskElement> fetchSchedulableTasksInRange( long idMin, long idMax, Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime );

    default void terminateService( long nGracefulShutdownMillis ) {
    }
}
