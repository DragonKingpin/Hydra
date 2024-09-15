package com.pinecone.hydra.config.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.GenericNamespaceNodeMeta;
import com.pinecone.hydra.config.distribute.entity.NamespaceNodeMeta;
import com.pinecone.hydra.config.distribute.source.ConfigNSNodeMetaManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface ConfigNSNodeMetaMapper extends ConfigNSNodeMetaManipulator {
    @Insert("INSERT INTO `hydra_conf_ns_node_meta` (`guid`) VALUES (#{guid})")
    void insert(NamespaceNodeMeta namespaceNodeMeta);
    @Delete("DELETE FROM `hydra_conf_ns_node_meta` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `id`, `guid` FROM `hydra_conf_ns_node_meta` WHERE guid=#{guid}")
    GenericNamespaceNodeMeta getNamespaceNodeMeta(GUID guid);

    void update(NamespaceNodeMeta namespaceNodeMeta);
}
