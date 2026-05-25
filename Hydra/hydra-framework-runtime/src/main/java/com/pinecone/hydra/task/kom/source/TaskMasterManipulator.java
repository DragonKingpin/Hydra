package com.pinecone.hydra.task.kom.source;

import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

public interface TaskMasterManipulator extends KOIMasterManipulator {

    TrieTreeManipulator getTrieTreeManipulator() ;

    TaskTreeDigestManipulator getTaskTreeDigestManipulator();

    AppNodeManipulator getAppNodeManipulator();

    TaskNodeManipulator getTaskNodeManipulator();

    TaskNamespaceManipulator getNamespaceManipulator();

    TireOwnerManipulator getTireOwnerManipulator();

    InstanceNodeManipulator getInstanceNodeManipulator();

}
