package com.pinecone.hydra.kernel.entity;

import com.pinecone.framework.util.id.GUID;

public interface KernelObject {
    int getEnumId();
    void setEnumId(int id);
    GUID getGuid();
    void setGuid(GUID guid);
    GUID getTargetGuid();
    void setTargetGuid(GUID targetGuid);
    String getTargetType();
    void setTargetType(String type);
    String getTargetSimpleType();
    void setTargetSimpleType(String targetSimpleType);
}
