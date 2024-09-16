package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.GenericConfigNode;
import com.pinecone.hydra.registry.source.RegistryNodeManipulator;
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
public interface RegistryNodeMapper extends RegistryNodeManipulator {
    @Insert("INSERT INTO `hydra_registry_config_node` (`guid`, `parent_guid`, `create_time`, `update_time`,`name`) VALUES (#{guid},#{parentGuid},#{createTime},#{updateTime},#{name})")
    void insert(ConfigNode configNode);

    @Delete("DELETE FROM `hydra_registry_config_node` WHERE `guid`=#{guid}")
    void remove(@Param("guid") GUID guid);

    @Select("SELECT `id`, `guid` , `parent_guid` AS parentGuid, `create_time` AS createTime, `update_time` AS updateTime, name FROM `hydra_registry_config_node` WHERE `guid`=#{guid}")
    GenericConfigNode getConfigurationNode( GUID guid );

    @Update("UPDATE hydra_registry_config_node SET parent_guid=#{parentGuid},create_time=#{createTime},update_time=#{updateTime} WHERE guid=#{guid}")
    void update( ConfigNode configNode);

    @Select("SELECT `guid` FROM `hydra_registry_config_node` WHERE `name`=#{name}")
    List<GUID> getNodeByName(String name);
}
