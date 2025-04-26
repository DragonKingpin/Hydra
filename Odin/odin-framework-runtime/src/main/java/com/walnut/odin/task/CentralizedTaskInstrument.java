package com.walnut.odin.task;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.walnut.odin.task.service.CategoryService;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.system.TaskPathInvalidException;

public interface CentralizedTaskInstrument extends KOMInstrument {

    RavenTaskConfig RAVEN_TASK_CONFIG = new GenericRavenTaskConfig();

    UniformTaskInstrument getUniformTaskInstrument();

    RavenTaskMasterManipulator getRavenTaskMasterManipulator();

    CategoryService getCategoryService();

    GUID assertGUIDByPath ( String taskTreePath ) throws TaskPathInvalidException;

    GUID assertTaskGUIDByPath ( String taskTreePath ) throws TaskPathInvalidException, IllegalArgumentException;

}
