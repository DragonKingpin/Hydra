package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceRuntimeNodeQuery;
import com.pinecone.hydra.service.kom.entity.GenericServiceRuntimeNodeEntity;
import com.pinecone.hydra.service.kom.entity.ServiceRuntimeNodeEntry;
import com.pinecone.hydra.service.kom.source.ServiceRuntimeNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ServiceRuntimeNodeMapper extends ServiceRuntimeNodeManipulator {

    @Override
    void createServiceRuntimeNode( ServiceRuntimeNodeEntry entry );

    @Override
    void updateServiceRuntimeNodeProfile( ServiceRuntimeNodeEntry entry );

    @Override
    void refreshServiceRuntimeNodeRuntime( ServiceRuntimeNodeEntry entry );

    @Override
    GenericServiceRuntimeNodeEntity queryServiceRuntimeNode( @Param( "guid" ) GUID guid );

    @Override
    GenericServiceRuntimeNodeEntity queryServiceRuntimeNodeByServiceGuidAndNodeId(
            @Param( "serviceGuid" ) GUID serviceGuid,
            @Param( "nodeId" ) String nodeId
    );

    List<GenericServiceRuntimeNodeEntity> fetchServiceRuntimeNodes0( @Param( "query" ) ServiceRuntimeNodeQuery query );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<ServiceRuntimeNodeEntry> fetchServiceRuntimeNodes( ServiceRuntimeNodeQuery query ) {
        ServiceRuntimeNodeQuery safeQuery = query;
        if ( safeQuery == null ) {
            safeQuery = new ServiceRuntimeNodeQuery();
        }

        return (List) this.fetchServiceRuntimeNodes0( safeQuery );
    }

    long countServiceRuntimeNodes0( @Param( "query" ) ServiceRuntimeNodeQuery query );

    @Override
    default long countServiceRuntimeNodes( ServiceRuntimeNodeQuery query ) {
        ServiceRuntimeNodeQuery safeQuery = query;
        if ( safeQuery == null ) {
            safeQuery = new ServiceRuntimeNodeQuery();
        }

        return this.countServiceRuntimeNodes0( safeQuery );
    }

    @Override
    void deleteServiceRuntimeNodesByServiceGuid( @Param( "serviceGuid" ) GUID serviceGuid );
}
