package com.pinecone.hydra.device.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericQuickElement;
import com.pinecone.hydra.device.kom.entity.QuickElement;
import com.pinecone.hydra.device.kom.source.QuickElementManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface QuickElementMapper extends QuickElementManipulator {

    @Override
    void insert( QuickElement quickElement );

    @Override
    void update( QuickElement quickElement );

    @Override
    void remove( @Param("guid") GUID guid );

    GenericQuickElement getQuickElement0( @Param("guid") GUID guid );

    @Override
    default GenericQuickElement getQuickElement( GUID guid, DeviceInstrument instrument ){
        GenericQuickElement element = this.getQuickElement0( guid );
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


    List<GenericQuickElement> fetchQuickElementByName0(@Param("name") String name );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<QuickElement> fetchQuickElementByName( String name ) {
        List<GenericQuickElement> list = this.fetchQuickElementByName0( name );
        return (List) list;
    }
}
