package com.pinecone.hydra.scenario.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;

public interface ScenarioMetaManipulatorSharer extends Pinenut {
    NamespaceNodeManipulator        getNamespaceNodeManipulator();

    NamespaceNodeMetaManipulator    getNamespaceNodeMetaManipulator();

    ScenarioCommonDataManipulator   getScenarioCommonDataManipulator();

}
