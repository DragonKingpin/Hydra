package com.pinecone.hydra.scenario.source;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ScenarioMetaManipulatorSharer extends Pinenut {
    NamespaceNodeManipulator        getNamespaceNodeManipulator();

    NamespaceNodeMetaManipulator    getNSNodeMetaManipulator();

    ScenarioCommonDataManipulator   getScenarioCommonDataManipulator();

}
