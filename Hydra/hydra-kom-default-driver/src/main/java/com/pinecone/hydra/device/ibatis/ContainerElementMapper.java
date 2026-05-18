package com.pinecone.hydra.device.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ContainerElement;
import com.pinecone.hydra.device.kom.entity.GenericContainerElement;
import com.pinecone.hydra.device.kom.source.ContainerElementManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
@IbatisDataAccessObject
public interface ContainerElementMapper extends ContainerElementManipulator {

    @Override
    void insert( ContainerElement containerElement );

    @Override
    void update( ContainerElement containerElement );

    @Override
    void remove( @Param("guid") GUID guid );

    GenericContainerElement getContainerElement0( @Param("guid") GUID guid );

    @Override
    default GenericContainerElement getContainerElement(GUID guid, DeviceInstrument instrument ){
        GenericContainerElement element = this.getContainerElement0( guid );
        if( element == null ) {
            return null;
        }
        element.apply( instrument );
        return element;
    }


    @Override
    List<GUID > getGuidsByName(@Param("name") String name );

    @Override
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );


    List<GenericContainerElement> fetchContainerElementByName0(@Param("name") String name );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<ContainerElement> fetchContainerElementByName( String name ) {
        List<GenericContainerElement> list = this.fetchContainerElementByName0( name );
        return (List) list;
    }
}
