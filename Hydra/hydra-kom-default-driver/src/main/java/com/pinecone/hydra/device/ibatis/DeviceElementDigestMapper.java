package com.pinecone.hydra.device.ibatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceElementDigestQuery;
import com.pinecone.hydra.device.kom.digest.DeviceElementDigest;
import com.pinecone.hydra.device.kom.source.DeviceElementDigestManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@Mapper
@IbatisDataAccessObject
public interface DeviceElementDigestMapper extends DeviceElementDigestManipulator {

    List<DeviceElementDigest> fetchDeviceElementDigests0( @Param( "query" ) DeviceElementDigestQuery query );

    List<DeviceElementDigest> fetchDeviceElementDigestsByGuids0( @Param( "guids" ) List<GUID> guids );

    long countDeviceElementDigests0( @Param( "query" ) DeviceElementDigestQuery query );

    @Override
    default List<DeviceElementDigest> fetchDeviceElementDigests( DeviceElementDigestQuery query ) {
        return this.fetchDeviceElementDigests0( this.safeQuery( query ) );
    }

    @Override
    default List<DeviceElementDigest> fetchDeviceElementDigestsByGuids( List<GUID> guids ) {
        if ( guids == null || guids.isEmpty() ) {
            return java.util.Collections.emptyList();
        }
        return this.fetchDeviceElementDigestsByGuids0( guids );
    }

    @Override
    default long countDeviceElementDigests( DeviceElementDigestQuery query ) {
        return this.countDeviceElementDigests0( this.safeQuery( query ) );
    }

    default DeviceElementDigestQuery safeQuery( DeviceElementDigestQuery query ) {
        return query == null ? new DeviceElementDigestQuery() : query;
    }
}
