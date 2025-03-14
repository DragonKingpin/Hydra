package com.pinecone.hydra.task.kom.source;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

public interface TaskMasterManipulator extends KOIMasterManipulator {
    TaskNodeManipulator getTaskNodeManipulator();
    CommonDataManipulator getCommonDataManipulator();
    NamespaceRulesManipulator getNamespaceRulesManipulator();
  TaskNamespaceManipulator getNamespaceManipulator();
   TaskMetaManipulator getTaskMetaManipulator();
    TrieTreeManipulator getTrieTreeManipulator() ;


/*    ApplicationNodeManipulator getApplicationNodeManipulator();

    ApplicationMetaManipulator getApplicationElementManipulator();*/
/*
    TaskNodeManipulator getServiceNodeManipulator();

    TaskMetaManipulator getServiceMetaManipulator();*/


    TireOwnerManipulator getTireOwnerManipulator();
}
