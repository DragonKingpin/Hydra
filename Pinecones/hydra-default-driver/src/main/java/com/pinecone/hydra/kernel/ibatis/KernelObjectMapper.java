package com.pinecone.hydra.kernel.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.kernel.entity.KernelObject;
import com.pinecone.hydra.kernel.source.KernelObjectManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import java.util.List;
@IbatisDataAccessObject
public interface KernelObjectMapper extends KernelObjectManipulator {
    void insert(KernelObject kernelObject);
    void remove(GUID guid, GUID targetGuid);
    void removeAll(GUID guid);
    KernelObject get(GUID guid,GUID targetGuid);
    List<KernelObject> getAll(GUID guid);
    void update(KernelObject kernelObject);
}
