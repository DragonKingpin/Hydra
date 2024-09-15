package com.pinecone.hydra.config.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.GenericNamespaceNode;
import com.pinecone.hydra.config.distribute.entity.NamespaceNode;
import com.pinecone.hydra.config.distribute.source.ConfigNSNodeManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ConfigNSNodeMapper extends ConfigNSNodeManipulator {
    @Insert("INSERT INTO `hydra_conf_namespace` (`guid`, `create_time`, `name`, `update_time`) VALUES (#{guid},#{createTime},#{name},#{updateTime})")
    void insert(NamespaceNode namespace);

    @Delete("DELETE FROM `hydra_conf_namespace` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid`, `create_time` AS createTime, `name`, `update_time` AS updateTime FROM `hydra_conf_namespace` WHERE guid=#{guid}")
    GenericNamespaceNode getNamespaceMeta(GUID guid);

    @Update("UPDATE `hydra_conf_namespace` SET `create_time`=#{createTime},`name`=#{name},`update_time`=#{updateTime} WHERE `guid`=#{guid}")
    void update(NamespaceNode namespace);

    @Select("SELECT `guid` FROM `hydra_conf_namespace` WHERE `name`=#{name}")
    List<GUID> getNodeByName(String name);
}
