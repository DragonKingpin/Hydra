package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.executum.Processum;

public interface Servgramium extends Servgram, Processum {
    @Override
    default void terminate() {
        this.apoptosis();
    }
}
