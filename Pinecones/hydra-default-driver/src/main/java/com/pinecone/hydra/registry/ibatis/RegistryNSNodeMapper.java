package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericNamespaceNode;
import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.registry.source.RegistryNSNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface RegistryNSNodeMapper extends RegistryNSNodeManipulator {
    @Insert("INSERT INTO `hydra_registry_namespace` (`guid`, `create_time`, `name`, `update_time`) VALUES (#{guid},#{createTime},#{name},#{updateTime})")
    void insert(NamespaceNode namespace);

    @Delete("DELETE FROM `hydra_registry_namespace` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid`, `create_time` AS createTime, `name`, `update_time` AS updateTime FROM `hydra_registry_namespace` WHERE guid=#{guid}")
    GenericNamespaceNode getNamespaceMeta(GUID guid);

    @Update("UPDATE `hydra_registry_namespace` SET `create_time`=#{createTime},`name`=#{name},`update_time`=#{updateTime} WHERE `guid`=#{guid}")
    void update(NamespaceNode namespace);

    @Select("SELECT `guid` FROM `hydra_registry_namespace` WHERE `name`=#{name}")
    List<GUID> getNodeByName(String name);
}
