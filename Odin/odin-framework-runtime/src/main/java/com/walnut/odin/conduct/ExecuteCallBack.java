package com.walnut.odin.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.entity.TaskElement;

import java.util.List;

public interface ExecuteCallBack extends Pinenut {
    List<TaskElement> introduceTask();

    void nextTask();
}
