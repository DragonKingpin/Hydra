package com.pinecone.hydra.registry.ibatis;

import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ElementNode;
import com.pinecone.hydra.registry.entity.GenericAttributes;
import com.pinecone.hydra.registry.entity.Attributes;
import com.pinecone.hydra.registry.source.RegistryAttributesManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface RegistryAttributesMapper extends RegistryAttributesManipulator {
    @Override
    void remove ( @Param("guid") GUID guid );

    @Override
    void insertAttribute( @Param("guid") GUID guid, @Param("key") String key, @Param("value") String value );

    @Override
    List<Map<String, Object > > getAttributesByGuid( @Param("guid") GUID guid );

    @Override
    void updateAttribute( @Param("guid") GUID guid, @Param("key") String key, @Param("value") String value );

    @Override
    default Attributes getAttributes( GUID guid, ElementNode element ) {
        List<Map<String, Object > > raws = this.getAttributesByGuid( guid );
        Attributes attributes = new GenericAttributes( guid, element, this );
        if ( raws.isEmpty() ) {
            return attributes;
        }

        for ( Map<String, Object > raw : raws ) {
            attributes.setAttribute( (String) raw.get( "key" ), (String) raw.get( "value" ) );
        }

        return attributes;
    }

    @Override
    boolean containsKey( @Param("guid") GUID guid, @Param("key") String key );

    @Override
    void clearAttributes( @Param("guid") GUID guid );

    @Override
    void removeAttributeWithValue( @Param("guid") GUID guid, @Param("key") String key, @Param("value") String value );

    @Override
    void removeAttribute( @Param("guid") GUID guid, @Param("key") String key );
}
