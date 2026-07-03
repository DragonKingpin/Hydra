package com.pinecone.hydra.service.kom.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.service.mapper.transaction.ServiceMappingTransaction;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

public interface ServiceMasterManipulator extends KOIMasterManipulator {
    TrieTreeManipulator getTrieTreeManipulator() ;

    ApplicationNodeManipulator getApplicationNodeManipulator();

    ServiceNodeManipulator getServiceNodeManipulator();

    ServiceNamespaceManipulator getNamespaceManipulator();

    ServiceInstanceManipulator getServiceInstanceManipulator();

    TireOwnerManipulator getTireOwnerManipulator();

    ServiceMappingTransaction transaction();

    void purgeServiceNode( GUID serviceGuid );

    void purgeServiceDirectory( GUID directoryGuid );

}
