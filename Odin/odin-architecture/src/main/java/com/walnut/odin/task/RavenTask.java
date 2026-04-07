package com.walnut.odin.task;

import com.pinecone.hydra.system.ups.UniformPyramidTask;
import com.pinecone.hydra.task.Task;
import com.pinecone.hydra.task.kom.entity.TaskElement;


public interface RavenTask extends Task, UniformPyramidTask {

    RavenTaskInstance createInstance();

    TaskElement getTaskElement();

}
