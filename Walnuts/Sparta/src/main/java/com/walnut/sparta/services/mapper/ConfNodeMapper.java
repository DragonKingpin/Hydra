package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.ConfNode;
import com.pinecone.hydra.config.distribute.entity.GenericConfNode;
import com.pinecone.hydra.config.distribute.source.ConfNodeManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ConfNodeMapper extends ConfNodeManipulator {
    @Insert("INSERT INTO `hydra_conf_configuration_node` (`guid`, `parent_guid`, `create_time`, `update_time`,`name`) VALUES (#{guid},#{parentGuid},#{createTime},#{updateTime},#{name})")
    void insert(ConfNode confNode);

    @Delete("DELETE FROM `hydra_conf_configuration_node` WHERE `guid`=#{guid}")
    void remove(@Param("guid") GUID guid);

    @Select("SELECT `id`, `guid` , `parent_guid` AS parentGuid, `create_time` AS createTime, `update_time` AS updateTime, name FROM `hydra_conf_configuration_node` WHERE `guid`=#{guid}")
    GenericConfNode getConfigurationNode(GUID guid);

    @Update("UPDATE hydra_conf_configuration_node SET parent_guid=#{parentGuid},create_time=#{createTime},update_time=#{updateTime} WHERE guid=#{guid}")
    void update( ConfNode confNode);

    @Select("SELECT `guid` FROM `hydra_conf_configuration_node` WHERE `name`=#{name}")
    List<GUID> getNodeByName(String name);
}
