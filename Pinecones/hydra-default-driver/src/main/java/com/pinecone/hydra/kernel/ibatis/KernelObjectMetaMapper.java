package com.pinecone.hydra.kernel.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.kernel.entity.KernelObjectMeta;
import com.pinecone.hydra.kernel.source.KernelObjectMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@IbatisDataAccessObject
public interface KernelObjectMetaMapper extends KernelObjectMetaManipulator {
    void insert(KernelObjectMeta kernelObjectMeta);
    void remove(GUID guid);
    KernelObjectMeta get(GUID guid);
    void update(KernelObjectMeta kernelObjectMeta);
    KernelObjectMeta getKOMByName(String name);
}
