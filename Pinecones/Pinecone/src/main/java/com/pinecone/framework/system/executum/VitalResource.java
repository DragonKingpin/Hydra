package com.pinecone.framework.system.executum;

import com.pinecone.framework.system.prototype.Pinenut;

public interface VitalResource extends Pinenut {
    String      getName();

    long        getId();

    Object      nativeResource();

    void        store(); // No exception

    TaskManager parentFates();
}
