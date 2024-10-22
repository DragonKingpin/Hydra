package com.pinecone.hydra.service;

import com.pinecone.framework.util.id.GUID;

public interface Application extends ServiceFamilyObject {
    long getEnumId();

    GUID getGuid();

    String getName();
}
