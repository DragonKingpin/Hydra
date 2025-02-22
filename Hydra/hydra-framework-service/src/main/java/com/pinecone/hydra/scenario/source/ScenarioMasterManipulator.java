package com.pinecone.hydra.scenario.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface ScenarioMasterManipulator extends KOIMasterManipulator {
    NamespaceNodeManipulator        getNamespaceNodeManipulator();

    NamespaceNodeMetaManipulator    getNSNodeMetaManipulator();

    ScenarioCommonDataManipulator   getScenarioCommonDataManipulator();

}
