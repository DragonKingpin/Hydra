package com.pinecone.hydra.task;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.entity.USII;

public interface TaskInstance extends Pinenut {
    Identification getId();

    USII getUSII();

    Object getProcessObject();

    Task getService();
}
