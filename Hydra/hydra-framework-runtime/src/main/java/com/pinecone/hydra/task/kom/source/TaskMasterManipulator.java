package com.pinecone.hydra.task.kom.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

public interface TaskMasterManipulator extends KOIMasterManipulator {
    TrieTreeManipulator getTrieTreeManipulator() ;

    CommonDataManipulator getCommonDataManipulator();

    JobNodeManipulator getJobNodeManipulator();

    JobMetaManipulator getApplicationElementManipulator();

    TaskNodeManipulator getTaskNodeManipulator();

    TaskMetaManipulator getTaskMetaManipulator();

    TaskNamespaceManipulator getNamespaceManipulator();

    NamespaceRulesManipulator getNamespaceRulesManipulator();

    TireOwnerManipulator getTireOwnerManipulator();

}
