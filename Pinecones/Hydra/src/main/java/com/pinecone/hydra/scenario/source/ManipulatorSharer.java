package com.pinecone.hydra.scenario.source;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ManipulatorSharer extends Pinenut {
    NamespaceNodeManipulator        getNamespaceNodeManipulator();

    NamespaceNodeMetaManipulator    getNamespaceNodeMetaManipulator();

    ScenarioCommonDataManipulator   getScenarioCommonDataManipulator();

    ScenarioTreeManipulator         getScenarioTreeManipulator();

    ScenarioNodePathManipulator     getScenarioNodePathManipulator();
}
