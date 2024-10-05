package com.pinecone.hydra.registry.ibatis;

import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ElementNode;
import com.pinecone.hydra.registry.entity.GenericAttributes;
import com.pinecone.hydra.registry.entity.Attributes;
import com.pinecone.hydra.registry.source.RegistryAttributesManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface RegistryAttributesMapper extends RegistryAttributesManipulator {
    @Override
    @Delete( "DELETE FROM `hydra_registry_node_attributes` WHERE `guid`=#{guid}" )
    void remove ( GUID guid );

    @Override
    @Insert( "INSERT INTO `hydra_registry_node_attributes` (`guid`, `key`, `value`) VALUES (#{guid}, #{key}, #{value})" )
    void insertAttribute( @Param("guid") GUID guid, @Param("key") String key, @Param("value") String value );

    @Override
    @Select( "SELECT `id` AS `enumId`, `guid`, `key`, `value` FROM `hydra_registry_node_attributes` WHERE `guid`=#{guid}" )
    List<Map<String, Object > > getAttributesByGuid( GUID guid );

    @Override
    @Update( "UPDATE `hydra_registry_node_attributes` SET `value`=#{value} WHERE `guid`=#{guid} AND `key`=#{key}")
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
    @Select( "SELECT COUNT(*) FROM `hydra_registry_node_attributes` WHERE `guid` = #{guid} AND `key` = #{key}" )
    boolean containsKey( @Param("guid") GUID guid, @Param("key") String key );

    @Override
    @Delete( "DELETE FROM `hydra_registry_node_attributes` WHERE `guid` = #{guid}" )
    void clearAttributes( @Param("guid") GUID guid );

    @Override
    @Delete( "DELETE FROM `hydra_registry_node_attributes` WHERE `guid` = #{guid} AND `key` = #{key} AND `value` = #{value}" )
    void removeAttributeWithValue( @Param("guid") GUID guid, @Param("key") String key, @Param("value") String value );

    @Override
    @Delete( "DELETE FROM `hydra_registry_node_attributes` WHERE `guid` = #{guid} AND `key` = #{key}" )
    void removeAttribute( @Param("guid") GUID guid, @Param("key") String key );
}
