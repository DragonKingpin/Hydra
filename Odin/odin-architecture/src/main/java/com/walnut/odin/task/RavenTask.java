package com.walnut.odin.task;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ups.UniformPyramidTask;
import com.pinecone.hydra.task.Task;
import com.walnut.odin.task.entity.RavenTaskElement;
import com.walnut.odin.task.entity.RavenTaskMeta;

public interface RavenTask extends Task, UniformPyramidTask {

    RavenTaskInstance createInstance();

    RavenTaskElement getTaskElement();

    RavenTaskMeta getExtraMeta();

    GUID getDeploySchemeId() ;

}
