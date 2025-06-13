package com.walnut.odin.ups;

import com.pinecone.hydra.system.ups.UniformPyramidTask;
import com.pinecone.hydra.task.Task;
import com.pinecone.hydra.task.kom.TaskInstrument;

public interface RavenTask extends Task, UniformPyramidTask {
    RavenInstance newInstance();
}
