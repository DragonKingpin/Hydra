package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.source.ScenarioNodePathManipulator;

public interface ScenarioNodePathMapper extends ScenarioNodePathManipulator {
    void insert(GUID guid, String path);

    void remove(GUID guid);

    String getPath(GUID guid);

    GUID getNode(String path);
}
