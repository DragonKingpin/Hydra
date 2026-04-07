package com.walnut.odin.task;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.walnut.odin.task.service.CategoryService;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.system.TaskPathInvalidException;

public interface CentralizedTaskInstrument extends TaskInstrument {

    RavenTaskConfig RAVEN_TASK_CONFIG = new GenericRavenTaskConfig();

    UniformTaskInstrument getUniformTaskInstrument();

    RavenTaskMasterManipulator getRavenTaskMasterManipulator();

    CategoryService getCategoryService();

    GUID assertGUIDByPath ( String taskTreePath ) throws TaskPathInvalidException;

    GUID assertTaskGUIDByPath ( String taskTreePath ) throws TaskPathInvalidException, IllegalArgumentException;







    RavenTask constructTask( TaskElement taskElement );

    RavenTask constructTask( TaskElement taskElement, @Nullable Identification serviceId );

    RavenTask createTask( TaskElement taskElement, @Nullable Identification serviceId );

}
