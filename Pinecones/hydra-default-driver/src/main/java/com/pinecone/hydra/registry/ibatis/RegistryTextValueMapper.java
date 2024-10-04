package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericTextValue;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.registry.source.RegistryTextValueManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface RegistryTextValueMapper extends RegistryTextValueManipulator {
    @Insert("INSERT INTO `hydra_registry_conf_node_text_value` (`guid`, `value`, `create_time`, `update_time`, `type`) VALUES (#{guid},#{value},#{createTime},#{updateTime},#{type})")
    void insert(TextValue textValue);

    @Delete("DELETE FROM `hydra_registry_conf_node_text_value` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid`, `value`, `create_time` AS createTime, `update_time` AS updateTime, `type` FROM `hydra_registry_conf_node_text_value` WHERE guid=#{guid}")
    GenericTextValue getTextValue(GUID guid);

    @Update("UPDATE `hydra_registry_conf_node_text_value` SET `value`=#{value}, `update_time`=#{updateTime}, `type`=#{type} WHERE guid=#{guid}")
    void update(TextValue textValue);

    @Insert("INSERT INTO `hydra_registry_conf_node_text_value` (`guid`, `type`, `create_time`, `update_time`, `value`) SELECT #{destinationGuid}, `type`, `create_time`, `update_time`, `value` FROM `hydra_registry_conf_node_text_value` WHERE `guid` = #{sourceGuid}")
    void copyTextValueTo(@Param("sourceGuid") GUID sourceGuid, @Param("destinationGuid") GUID destinationGuid);
}
