package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface RegistryPropertiesMapper extends RegistryPropertiesManipulator {
    @Insert("INSERT INTO hydra_registry_conf_node_properties (`guid`, `key`, `type`, `create_time`, `update_time`, `value`) VALUES (#{guid},#{key},#{type},#{createTime},#{updateTime},#{rawValue})")
    void insert( Property property );

    @Delete("DELETE FROM `hydra_registry_conf_node_properties` WHERE `guid`=#{guid} AND `key`=#{key}")
    void remove( GUID guid, String key );

    @Select("SELECT `id`, `guid`, `key`, `type`, `create_time` AS createTime, `update_time` AS updateTime, `value` AS rawValue FROM hydra_registry_conf_node_properties WHERE `guid`=#{guid}")
    List<GenericProperty > getProperties0( GUID guid );

    @SuppressWarnings( "unchecked" )
    default List<Property > getProperties( GUID guid ) {
        return (List) this.getProperties0( guid );
    }

    @Update( "UPDATE `hydra_registry_conf_node_properties` SET `key`=#{key}, `type`=#{type}, update_time=#{updateTime}, value=#{rawValue} WHERE `guid`=#{guid} AND `key`=#{key}" )
    void update( Property property );

    @Delete("DELETE FROM `hydra_registry_conf_node_properties` WHERE `guid` = #{guid}")
    void removeAll( GUID guid );
    @Insert("INSERT INTO `hydra_registry_conf_node_properties` (`guid`, `key`, `type`, `create_time`, `update_time`, `value`) SELECT\n" +
            "\t#{destinationGuid},\n" +
            "\t`key`,\n" +
            "\t`type`,\n" +
            "\t`create_time`,\n" +
            "\t`update_time`,\n" +
            "\t`value` \n" +
            "FROM\n" +
            "\t`hydra_registry_conf_node_properties` AS src \n" +
            "WHERE\n" +
            "\t`guid` = #{sourceGuid} \n" +
            "\tAND NOT EXISTS ( \n" +
            "\tSELECT `guid` FROM `hydra_registry_conf_node_properties` AS dest WHERE dest.`guid` = #{destinationGuid} AND dest.`key` = src.`key` \n" +
            "\t)")
    void copyPropertiesTo( @Param("sourceGuid") GUID sourceGuid, @Param("destinationGuid") GUID destinationGuid);
}
