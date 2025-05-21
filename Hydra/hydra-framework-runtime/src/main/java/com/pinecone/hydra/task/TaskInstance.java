package com.pinecone.hydra.task;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;

public interface TaskInstance extends Pinenut {
    Identification getId();

    Object getProcessObject();

    Task getAffiliatedTask();
}
