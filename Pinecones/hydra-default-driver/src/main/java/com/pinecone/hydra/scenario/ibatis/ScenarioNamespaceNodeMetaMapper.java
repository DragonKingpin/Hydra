package com.pinecone.hydra.scenario.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.entity.GenericNamespaceNodeMeta;
import com.pinecone.hydra.scenario.entity.NamespaceNodeMeta;
import com.pinecone.hydra.scenario.source.NamespaceNodeMetaManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface ScenarioNamespaceNodeMetaMapper extends NamespaceNodeMetaManipulator {
    @Insert("INSERT INTO `hydra_scenario_namespace_node_meta` (`guid`) VALUES (#{guid})")
    void insert(NamespaceNodeMeta namespaceNodeMeta);

    @Delete("DELETE FROM `hydra_scenario_namespace_node_meta` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid` FROM `hydra_scenario_namespace_node_meta` WHERE `guid`=#{guid}")
    GenericNamespaceNodeMeta getNamespaceNodeMeta(GUID guid);

    void update(NamespaceNodeMeta namespaceNodeMeta);
}
