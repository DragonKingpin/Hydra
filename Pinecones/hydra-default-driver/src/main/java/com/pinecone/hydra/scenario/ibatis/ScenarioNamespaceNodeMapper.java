package com.pinecone.hydra.scenario.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.entity.GenericNamespaceNode;
import com.pinecone.hydra.scenario.entity.NamespaceNode;
import com.pinecone.hydra.scenario.source.NamespaceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
@IbatisDataAccessObject
public interface ScenarioNamespaceNodeMapper extends NamespaceNodeManipulator {
    @Insert("INSERT INTO `hydra_scenario_namespace_node` (`guid`, `name`) VALUES (#{guid},#{name})")
    void insert(NamespaceNode namespaceNode);

    @Delete("DELETE FROM `hydra_scenario_namespace_node` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid`, `name` FROM `hydra_scenario_namespace_node` WHERE guid=#{guid}")
    GenericNamespaceNode getNamespaceNode(GUID guid);

    void update(NamespaceNode namespaceNode);

    @Select("SELECT guid FROM hydra_scenario_namespace_node where name=#{name}")
    List<GUID> getGuidsByName(String name);
}
