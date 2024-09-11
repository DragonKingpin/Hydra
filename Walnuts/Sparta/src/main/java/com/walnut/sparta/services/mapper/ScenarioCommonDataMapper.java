package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.entity.GenericScenarioCommonData;
import com.pinecone.hydra.scenario.entity.ScenarioCommonData;
import com.pinecone.hydra.scenario.source.ScenarioCommonDataManipulator;

public interface ScenarioCommonDataMapper extends ScenarioCommonDataManipulator {
    void insert(ScenarioCommonData scenarioCommonData);

    void remove(GUID guid);

    GenericScenarioCommonData getScenarioCommonData(GUID guid);

    void update(ScenarioCommonData scenarioCommonData);
}
