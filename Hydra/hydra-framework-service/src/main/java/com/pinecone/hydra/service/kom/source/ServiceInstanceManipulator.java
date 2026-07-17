package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceInstanceQuery;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceInstanceManipulator extends Pinenut {
    void initServiceInstance( ServiceInstanceEntry element );

    ServiceInstanceEntry queryServiceInstance( GUID instanceId );

    List<ServiceInstanceEntry> fetchServiceInstances( ServiceInstanceQuery query );

    long countServiceInstances( ServiceInstanceQuery query );

    List<ServiceInstanceEntry> fetchServiceInstancesByServiceGuid( GUID serviceGuid );

    List<ServiceInstanceEntry> fetchServiceInstancesByStatusAfterId( String status, long lastId, int limit );

    void updateServiceInstance( ServiceInstanceEntry element );

    int updateServiceInstanceStatusIfCurrentStatus(
            GUID instanceGuid,
            String expectedStatus,
            String status,
            LocalDateTime offlineTime,
            LocalDateTime latestEndTime
    );

    void deleteServiceInstancesByServiceGuid( GUID serviceGuid );
}
