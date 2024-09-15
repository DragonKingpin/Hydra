package com.pinecone.hydra.config.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ConfigTreeMapper extends ScopeTreeManipulator {
    default void insert (GUIDDistributedScopeNode distributedConfTreeNode){
        List<GUID> parentGuids = distributedConfTreeNode.getParentGUIDs();
        this.insertTreeNode(distributedConfTreeNode.getGuid(),distributedConfTreeNode.getType(),distributedConfTreeNode.getBaseDataGUID(),distributedConfTreeNode.getNodeMetadataGUID());
        if (parentGuids!=null){
            for(GUID parentGuid : distributedConfTreeNode.getParentGUIDs()){
                this.insertParentNode(distributedConfTreeNode.getGuid(),parentGuid);
            }
        }

    }
    @Insert("INSERT INTO hydra_conf_meta_map (`guid`, `type`,`base_data_guid`,`node_meta_guid`) VALUES (#{guid},#{type},#{baseDataGuid},#{nodeMetaGuid})")
    void insertTreeNode(@Param("guid") GUID guid,@Param("type") UOI type,@Param("baseDataGuid") GUID baseDataGuid,@Param("nodeMetaGuid") GUID nodeMetaGuid);
    @Insert("INSERT INTO hydra_conf_node_tree (guid, parent_guid) VALUES (#{guid},#{parentGuid})")
    void insertParentNode(@Param("guid")GUID guid,@Param("parentGuid")GUID parentGuid);

    default GUIDDistributedScopeNode getNode(GUID guid){
        GUIDDistributedScopeNode node = this.getMeta(guid);
        List<GUID> parent = this.getParentNodes(guid);
        node.setParentGUID(parent);
        return node;
    }
    @Select("SELECT `id`, `guid`, `type`,base_data_guid AS baseDataGUID,node_meta_guid AS nodeMetadataGUID  FROM hydra_conf_meta_map WHERE guid=#{guid}")
    GUIDDistributedScopeNode getMeta(GUID guid);

    default void remove(GUID guid){
        this.removeMeta(guid);
        this.removeParentNode(guid);
        this.removeOwner(guid);
        this.removeSubordinate(guid);
    }
    @Delete("DELETE FROM `hydra_conf_meta_map` WHERE `guid`=#{guid}")
    void removeMeta(GUID guid);
    @Delete("DELETE FROM `hydra_conf_node_tree` WHERE `guid`=#{guid}")
    void removeParentNode(GUID guid);
    @Delete("DELETE FROM `hydra_conf_node_owner` WHERE `subordinate_guid`=#{guid}")
    void removeOwner(GUID guid);
    @Delete("DELETE FROM `hydra_conf_node_owner` WHERE `owner_guid`=#{guid}")
    void removeSubordinate(GUID guid);

    @Delete("DELETE FROM `hydra_conf_node_tree` WHERE `guid`=#{chileGuid} AND `parent_guid`=#{parentGuid}")
    void removeInheritance(@Param("chileGuid") GUID childGuid,@Param("parentGUid") GUID parentGuid);

    @Select("SELECT `id`, `guid`, `parent_guid` AS parentGuid FROM `hydra_conf_node_tree` WHERE `parent_guid`=#{guid}")
    List<GUIDDistributedScopeNode> getChild(GUID guid);

    @Select("SELECT `parent_guid` FROM `hydra_conf_node_tree` WHERE `guid`=#{guid}")
    List<GUID> getParentNodes(GUID guid);
}
