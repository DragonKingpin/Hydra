package com.pinecone.hydra.kernel.entity;

import com.pinecone.framework.util.id.GUID;

public interface KernelObjectMeta {
     int getEnumId();
     void setEnumId(int id);
     GUID getGuid();
     void setGuid(GUID guid);
     String getName();
     void setName(String name);
}
