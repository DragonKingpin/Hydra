package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceInstanceQuery;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.kom.source.ServiceInstanceManipulator;
import com.pinecone.hydra.service.kom.entity.GenericServiceInstanceEntity;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ServiceInstanceMapper extends ServiceInstanceManipulator {
    @Override
    void initServiceInstance(ServiceInstanceEntry element);

    @Override
    GenericServiceInstanceEntity queryServiceInstance( @Param("instanceId") GUID instanceId );

    List<GenericServiceInstanceEntity> fetchServiceInstances0( @Param( "query" ) ServiceInstanceQuery query );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<ServiceInstanceEntry> fetchServiceInstances( ServiceInstanceQuery query ) {
        ServiceInstanceQuery safeQuery = query;
        if ( safeQuery == null ) {
            safeQuery = new ServiceInstanceQuery();
        }

        return (List) this.fetchServiceInstances0( safeQuery );
    }

    @Override
    default long countServiceInstances( ServiceInstanceQuery query ) {
        ServiceInstanceQuery safeQuery = query;
        if ( safeQuery == null ) {
            safeQuery = new ServiceInstanceQuery();
        }

        return this.countServiceInstances0( safeQuery );
    }

    long countServiceInstances0( @Param( "query" ) ServiceInstanceQuery query );

    List<GenericServiceInstanceEntity> fetchServiceInstancesByServiceGuid0( @Param( "serviceGuid" ) GUID serviceGuid );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<ServiceInstanceEntry> fetchServiceInstancesByServiceGuid( GUID serviceGuid ) {
        return (List) this.fetchServiceInstancesByServiceGuid0( serviceGuid );
    }

    @Override
    void updateServiceInstance(ServiceInstanceEntry element);

    @Override
    void deleteServiceInstancesByServiceGuid( @Param( "serviceGuid" ) GUID serviceGuid );
}
