package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceRuntimeNodeQuery;
import com.pinecone.hydra.service.kom.entity.ServiceRuntimeNodeEntry;

import java.util.List;

public interface ServiceRuntimeNodeManipulator extends Pinenut {

    void createServiceRuntimeNode( ServiceRuntimeNodeEntry entry );

    void updateServiceRuntimeNodeProfile( ServiceRuntimeNodeEntry entry );

    void refreshServiceRuntimeNodeRuntime( ServiceRuntimeNodeEntry entry );

    ServiceRuntimeNodeEntry queryServiceRuntimeNode( GUID guid );

    ServiceRuntimeNodeEntry queryServiceRuntimeNodeByServiceGuidAndNodeId( GUID serviceGuid, String nodeId );

    List<ServiceRuntimeNodeEntry> fetchServiceRuntimeNodes( ServiceRuntimeNodeQuery query );

    long countServiceRuntimeNodes( ServiceRuntimeNodeQuery query );

    void deleteServiceRuntimeNodesByServiceGuid( GUID serviceGuid );
}
