package com.walnut.odin.dispatch;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public interface DispatchStrategy extends Pinenut {

    Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> dispatch(
            Collection<TaskExecutionProcessor> processors, Collection<TaskLaunchContext> contexts, TaskDispatcher dispatcher
    ) throws TaskDispatchException;

}
