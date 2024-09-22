package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNodeMeta;
import com.pinecone.hydra.registry.entity.GenericConfigNodeMeta;
import com.pinecone.hydra.registry.source.RegistryNodeMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface RegistryNodeMetaMapper extends RegistryNodeMetaManipulator {
    @Insert("INSERT INTO `hydra_registry_conf_node_meta` (`guid`) VALUES (#{guid})")
    void insert(ConfigNodeMeta configNodeMeta);
    @Delete("DELETE FROM `hydra_registry_conf_node_meta` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `id`, `guid` FROM `hydra_registry_conf_node_meta` WHERE `guid`=#{guid}")
    GenericConfigNodeMeta getConfigNodeMeta(GUID guid);

    void update(ConfigNodeMeta configNodeMeta);
}
