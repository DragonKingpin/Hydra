package com.pinecone.hydra.kernel.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.kernel.entity.KernelObject;
import com.pinecone.hydra.kernel.entity.KernelObjectMeta;

public interface KernelObjectMetaManipulator {
    void insert(KernelObjectMeta kernelObjectMeta);
    void remove(GUID guid);
    KernelObjectMeta get(GUID guid);
    void update(KernelObjectMeta kernelObjectMeta);
    KernelObjectMeta getKOMByName(String name);
}
