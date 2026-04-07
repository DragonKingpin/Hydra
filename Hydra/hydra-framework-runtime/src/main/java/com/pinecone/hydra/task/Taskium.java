package com.pinecone.hydra.task;

import com.pinecone.framework.system.executum.Processum;

public interface Taskium extends TaskInstance {
    @Override
    Processum getProcessObject();
}
