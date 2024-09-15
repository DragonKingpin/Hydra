package com.pinecone.hydra.scenario.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;
@Mapper
public interface ScenarioTreeMapper extends ScopeTreeManipulator {
    @Insert("INSERT INTO hydra_scenario_node_map (guid, type, base_data_guid, node_meta_guid) VALUES (#{guid},#{type},#{baseDataGUID},#{nodeMetadataGUID})")
    void insert (GUIDDistributedScopeNode distributedConfTreeNode);
    default GUIDDistributedScopeNode getNode(GUID guid){
        GUIDDistributedScopeNode metaNode = this.getMetaNode(guid);
        List<GUID> parentNodes = this.getParentNodes(guid);
        if (parentNodes != null){
            metaNode.setParentGUID(parentNodes);
        }else {
            metaNode.setParentGUID(new ArrayList<GUID>());
        }
        return metaNode;
    }
    @Select("SELECT id, guid, type, base_data_guid AS baseDataGUID, node_meta_guid AS nodeMetadataGUID FROM hydra_scenario_node_map WHERE guid=#{guid}")
    GUIDDistributedScopeNode getMetaNode(GUID guid);

    default void remove(GUID guid){
        removeMeta(guid);
        removeParentNode(guid);
    }
    @Delete("DELETE FROM hydra_scenario_node_map WHERE guid=#{guid}")
    void removeMeta(GUID guid);
    @Delete("DELETE FROM hydra_scenario_node_tree WHERE guid=#{guid}")
    void removeParentNode(GUID guid);
    @Delete("DELETE FROM `hydra_scenario_node_tree` WHERE `guid`=#{childGuid} AND `parent_guid`=#{parentGuid}")
    void removeInheritance(@Param("childGuid") GUID childGuid, @Param("parentGuid") GUID parentGuid);
    @Select("SELECT `parent_guid` FROM `hydra_scenario_node_tree` WHERE `guid`=#{guid}")
    List<GUID> getParentNodes(GUID guid);
    @Select("SELECT `path` FROM `hydra_scenario_node_path` WHERE `guid`=#{guid}")
    String getPath(GUID guid);

    void updatePath( GUID guid, String path);
    @Select("SELECT `guid` FROM `hydra_scenario_node_path` WHERE `path`=#{path}")
    GUID parsePath(String path);

    void insertNodeToParent(GUID nodeGUID,GUID parentGUID);
    @Select("SELECT guid FROM hydra_scenario_node_tree WHERE parent_guid=#{guid}")
    List<GUIDDistributedScopeNode> getChild(GUID guid);
    @Delete("DELETE FROM `hydra_scenario_node_path` WHERE `guid`=#{guid}")
    void removePath(GUID guid);

    void putNode(GUID guid,GUIDDistributedScopeNode distributedTreeNode);

    long size();

}
