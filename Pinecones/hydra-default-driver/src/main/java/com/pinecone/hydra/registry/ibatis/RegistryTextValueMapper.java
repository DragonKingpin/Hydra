package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.registry.source.RegistryTextValueManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface RegistryTextValueMapper extends RegistryTextValueManipulator {
    @Insert("INSERT INTO `hydra_conf_conf_node_text_value` (`guid`, `value`, `create_time`, `update_time`, `type`) VALUES (#{guid},#{value},#{create_time},#{update_time},#{type})")
    void insert(TextValue textValue);

    @Delete("DELETE FROM `hydra_conf_conf_node_text_value` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid`, `value`, `create_time` AS createTime, `update_time` AS updateTime, `type` FROM `hydra_conf_conf_node_text_value` WHERE guid=#{guid}")
    TextValue getTextValue(GUID guid);

    void update(TextValue textValue);
}
