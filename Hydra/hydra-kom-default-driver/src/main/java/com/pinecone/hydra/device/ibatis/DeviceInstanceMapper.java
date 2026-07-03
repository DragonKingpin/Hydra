package com.pinecone.hydra.device.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstanceQuery;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.kom.instance.GenericDeviceInstanceEntry;
import com.pinecone.hydra.device.kom.source.DeviceInstanceManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface DeviceInstanceMapper extends DeviceInstanceManipulator {

    @Override
    void initDeviceInstance( DeviceInstanceEntry element );

    @Override
    GenericDeviceInstanceEntry queryDeviceInstance( @Param( "instanceGuid" ) GUID instanceGuid );

    List<GenericDeviceInstanceEntry> fetchDeviceInstances0( @Param( "query" ) DeviceInstanceQuery query );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<DeviceInstanceEntry> fetchDeviceInstances( DeviceInstanceQuery query ) {
        DeviceInstanceQuery safeQuery = query == null ? new DeviceInstanceQuery() : query;
        return (List) this.fetchDeviceInstances0( safeQuery );
    }

    @Override
    default long countDeviceInstances( DeviceInstanceQuery query ) {
        DeviceInstanceQuery safeQuery = query == null ? new DeviceInstanceQuery() : query;
        return this.countDeviceInstances0( safeQuery );
    }

    long countDeviceInstances0( @Param( "query" ) DeviceInstanceQuery query );

    List<GenericDeviceInstanceEntry> fetchDeviceInstancesByDeviceGuid0( @Param( "deviceGuid" ) GUID deviceGuid );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<DeviceInstanceEntry> fetchDeviceInstancesByDeviceGuid( GUID deviceGuid ) {
        return (List) this.fetchDeviceInstancesByDeviceGuid0( deviceGuid );
    }

    List<GenericDeviceInstanceEntry> fetchDeviceInstancesByOwnerInstanceGuid0( @Param( "ownerInstanceGuid" ) GUID ownerInstanceGuid );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<DeviceInstanceEntry> fetchDeviceInstancesByOwnerInstanceGuid( GUID ownerInstanceGuid ) {
        return (List) this.fetchDeviceInstancesByOwnerInstanceGuid0( ownerInstanceGuid );
    }

    @Override
    void updateDeviceInstance( DeviceInstanceEntry element );
}
