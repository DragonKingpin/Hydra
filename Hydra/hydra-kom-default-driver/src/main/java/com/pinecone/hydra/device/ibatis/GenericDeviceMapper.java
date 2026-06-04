package com.pinecone.hydra.device.ibatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericDeviceElement;
import com.pinecone.hydra.device.kom.source.GenericDeviceManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface GenericDeviceMapper extends GenericDeviceManipulator {

    @Override
    void insert( GenericDeviceElement genericDeviceElement );

    @Override
    void update( GenericDeviceElement genericDeviceElement );

    @Override
    void remove( @Param("guid") GUID guid );

    GenericDeviceElement getGenericDeviceElement0( @Param("guid") GUID guid );

    @Override
    default GenericDeviceElement getGenericDeviceElement( GUID guid, DeviceInstrument instrument ) {
        GenericDeviceElement element = this.getGenericDeviceElement0( guid );
        if ( element == null ) {
            return null;
        }
        element.apply( instrument );
        return element;
    }

    @Override
    List<GUID> getGuidsByName( @Param("name") String name );

    @Override
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
