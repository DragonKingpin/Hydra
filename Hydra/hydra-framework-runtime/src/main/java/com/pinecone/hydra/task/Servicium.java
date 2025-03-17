package com.pinecone.hydra.task;

import com.pinecone.framework.system.executum.Processum;

public interface Servicium extends TaskInstance {
    @Override
    Processum getProcessObject();
}
