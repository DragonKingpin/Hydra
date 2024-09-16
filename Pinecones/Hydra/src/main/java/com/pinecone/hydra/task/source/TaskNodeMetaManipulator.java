package com.pinecone.hydra.task.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.entity.TaskCommonData;
import com.pinecone.hydra.task.entity.TaskNodeMeta;

public interface TaskNodeMetaManipulator extends Pinenut {
    void insert(TaskNodeMeta taskNodeMeta);

    void remove(GUID guid);

    TaskNodeMeta getTaskNodeMeta(GUID guid);

    void update(TaskNodeMeta taskNodeMeta);
}
