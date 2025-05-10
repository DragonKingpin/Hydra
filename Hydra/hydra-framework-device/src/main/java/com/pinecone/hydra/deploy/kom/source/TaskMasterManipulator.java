package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

public interface TaskMasterManipulator extends KOIMasterManipulator {
    TrieTreeManipulator getTrieTreeManipulator() ;

    NodeMetaManipulator getNodeMetaManipulator();

    JobNodeManipulator getJobNodeManipulator();

    TaskNodeManipulator getTaskNodeManipulator();

    TaskNamespaceManipulator getNamespaceManipulator();

    NamespaceRulesManipulator getNamespaceRulesManipulator();

    TireOwnerManipulator getTireOwnerManipulator();

}
