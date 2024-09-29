package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface RegistryPropertiesMapper extends RegistryPropertiesManipulator {
    @Insert("INSERT INTO hydra_registry_conf_node_properties (`guid`, `key`, type, create_time, update_time, value) VALUES (#{guid},#{key},#{type},#{createTime},#{updateTime},#{value})")
    void insert( Property property );

    @Delete("DELETE FROM `hydra_registry_conf_node_properties` WHERE `guid`=#{guid} AND `key`=#{key}")
    void remove( GUID guid, String key );

    @Select("SELECT `id`, `guid`, `key`, `type`, `create_time` AS createTime, `update_time` AS updateTime, `value` FROM hydra_registry_conf_node_properties WHERE `guid`=#{guid}")
    List<GenericProperty > getProperties0( GUID guid );

    @SuppressWarnings( "unchecked" )
    default List<Property > getProperties( GUID guid ) {
        return (List) this.getProperties0( guid );
    }

    @Update( "UPDATE `hydra_registry_conf_node_properties` SET `key`=#{key}, `type`=#{type}, update_time=#{updateTime}, value=#{value} WHERE `guid`=#{guid} AND `key`=#{key}" )
    void update( Property property );
}
