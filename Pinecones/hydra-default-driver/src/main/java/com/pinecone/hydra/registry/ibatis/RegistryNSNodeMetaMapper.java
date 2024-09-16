package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericNamespaceNodeMeta;
import com.pinecone.hydra.registry.entity.NamespaceNodeMeta;
import com.pinecone.hydra.registry.source.RegistryNSNodeMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface RegistryNSNodeMetaMapper extends RegistryNSNodeMetaManipulator {
    @Insert("INSERT INTO `hydra_registry_ns_node_meta` (`guid`) VALUES (#{guid})")
    void insert(NamespaceNodeMeta namespaceNodeMeta);
    @Delete("DELETE FROM `hydra_registry_ns_node_meta` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `id`, `guid` FROM `hydra_registry_ns_node_meta` WHERE guid=#{guid}")
    GenericNamespaceNodeMeta getNamespaceNodeMeta(GUID guid);

    void update(NamespaceNodeMeta namespaceNodeMeta);
}
