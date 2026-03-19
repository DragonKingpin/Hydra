package com.pinecone.hydra.task;

import com.pinecone.framework.util.id.GUID;

public interface App extends TaskFamilyMeta {
    long getEnumId();

    GUID getGuid();

    String getName();
}
