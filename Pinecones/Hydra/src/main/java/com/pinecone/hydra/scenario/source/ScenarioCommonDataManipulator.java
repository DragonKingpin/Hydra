package com.pinecone.hydra.scenario.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.entity.NamespaceNodeMeta;
import com.pinecone.hydra.scenario.entity.ScenarioCommonData;

public interface ScenarioCommonDataManipulator extends Pinenut {
    void insert(ScenarioCommonData scenarioCommonData);

    void remove(GUID guid);

    ScenarioCommonData getScenarioCommonData(GUID guid);

    void update(ScenarioCommonData scenarioCommonData);
}
