package com.pinecone.hydra.device.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.kom.source.PhysicalHostManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface PhysicalHostMapper extends PhysicalHostManipulator {

    @Override
    void insert( PhysicalHostElement physicalHostElement );

    @Override
    void update( PhysicalHostElement serviceElement );

    @Override
    void remove(@Param("guid") GUID guid);

    GenericPhysicalHostElement getPhysicalHostElement0( @Param("guid") GUID guid );

    @Override
    default GenericPhysicalHostElement getPhysicalHostElement( GUID guid, DeviceInstrument instrument ){
        GenericPhysicalHostElement element = this.getPhysicalHostElement0( guid );
        if( element == null ) {
            return null;
        }
        element.apply( instrument );
        return element;
    }

    @Override
    List<GUID> getGuidsByName(@Param("name") String name );

    @Override
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );

}
