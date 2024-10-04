package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericNodeAttribute;
import com.pinecone.hydra.registry.entity.NodeAttribute;
import com.pinecone.hydra.registry.source.RegistryCommonDataManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface RegistryCommonDataMapper extends RegistryCommonDataManipulator {
    @Insert("INSERT INTO `hydra_registry_node_attribute` (`guid`) VALUES (#{guid})")
    void insert(NodeAttribute nodeAttribute);

    @Delete("DELETE FROM `hydra_registry_node_attribute` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id` AS `enumId`, `guid` FROM `hydra_registry_node_attribute` WHERE `guid`=#{guid}")
    GenericNodeAttribute getNodeCommonData(GUID guid);

    void update(NodeAttribute nodeAttribute);
}
