package com.pinecone.hydra.scenario.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface ScenarioNodePathManipulator extends Pinenut {
    void insert(GUID guid, String path);

    void remove(GUID guid);

    String getPath(GUID guid);

    GUID getNode(String path);
}