package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceElementQuery;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface ServiceNodeManipulator extends GUIDNameManipulator {
    //ServiceNode的CRUD
    void insert( ServiceElement serviceNode );

    void remove(GUID UUID);

    ServiceElement getServiceNode(GUID UUID);

    void update(ServiceElement serviceNode);

    List<ServiceElement> fetchServiceNodeByName(String name);

    @Override
    List<GUID> getGuidsByName(String name);

    @Override
    List<GUID> getGuidsByNameID(String name, GUID guid);

    List<ServiceElement> fetchAllService();

    List<ServiceElement> fetchServices( ServiceElementQuery query );

    long countServices( ServiceElementQuery query );
}
