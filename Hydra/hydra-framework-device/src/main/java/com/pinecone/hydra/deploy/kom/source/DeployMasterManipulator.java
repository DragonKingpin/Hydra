package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

public interface DeployMasterManipulator extends KOIMasterManipulator {
    TrieTreeManipulator getTrieTreeManipulator() ;

    ClusterNodeManipulator getJobNodeManipulator();

    DeployNamespaceManipulator getNamespaceManipulator();

    TireOwnerManipulator getTireOwnerManipulator();

    PhysicalHostManipulator getPhysicalHostManipulator();

    VirtualMachineManipulator getVirtualMachineManipulator();

    QuickElementManipulator getQuickElementManipulator();

    ContainerElementManipulator getContainerElementManipulator();
}
