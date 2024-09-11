package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.GenericProperties;
import com.pinecone.hydra.config.distribute.entity.Properties;
import com.pinecone.hydra.config.distribute.source.PropertiesManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PropertiesMapper extends PropertiesManipulator {
    @Insert("INSERT INTO hydra_conf_conf_node_properties (`guid`, `key`, type, create_time, update_time, value) VALUES (#{guid},#{key},#{type},#{createTime},#{updateTime},#{value})")
    void insert(Properties properties);

    @Delete("DELETE FROM `hydra_conf_conf_node_properties` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid`, `key`, `type`, `create_time` AS createTime, `update_time` AS updateTime, `value` FROM hydra_conf_conf_node_properties WHERE `guid`=#{guid}")
    List<GenericProperties> getProperties(GUID guid);

    void update(Properties properties);
}
