package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericProperties;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface RegistryPropertiesMapper extends RegistryPropertiesManipulator {
    @Insert("INSERT INTO hydra_registry_conf_node_properties (`guid`, `key`, type, create_time, update_time, value) VALUES (#{guid},#{key},#{type},#{createTime},#{updateTime},#{value})")
    void insert(Properties properties);

    @Delete("DELETE FROM `hydra_registry_conf_node_properties` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid`, `key`, `type`, `create_time` AS createTime, `update_time` AS updateTime, `value` FROM hydra_registry_conf_node_properties WHERE `guid`=#{guid}")
    List<GenericProperties> getProperties(GUID guid);

    void update(Properties properties);
}
