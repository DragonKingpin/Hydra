package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfNodeMeta;
import com.pinecone.hydra.registry.entity.GenericConfNodeMeta;
import com.pinecone.hydra.registry.source.RegistryNodeMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface RegistryNodeMetaMapper extends RegistryNodeMetaManipulator {
    @Insert("INSERT INTO `hydra_conf_conf_node_meta` (`guid`) VALUES (#{guid})")
    void insert(ConfNodeMeta confNodeMeta);
    @Delete("DELETE FROM `hydra_conf_conf_node_meta` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `id`, `guid` FROM `hydra_conf_conf_node_meta` WHERE `guid`=#{guid}")
    GenericConfNodeMeta getConfNodeMeta(GUID guid);

    void update(ConfNodeMeta confNodeMeta);
}
