package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ServiceTrieTreeMapper extends TrieTreeManipulator {
    default void insert(GUIDDistributedTrieNode node){
        this.putNodeMeta(node);
        if (node.getParentGUIDs()==null) return;
        if ( !node.getParentGUIDs().isEmpty() ){
            for ( GUID guid:node.getParentGUIDs() ){
                this.putTreeNode( node.getGuid(), guid );
            }
        }
    }

    @Insert("INSERT INTO `hydra_service_meta_map` (`guid`, `base_data_guid`, `node_metadata_guid`, `type`) VALUES (#{guid},#{baseDataGUID},#{nodeMetadataGUID},#{type})")
    void putNodeMeta(GUIDDistributedTrieNode node);

    @Insert("INSERT INTO `hydra_service_node_tree` (`guid`, `parent_guid`) VALUES (#{guid},#{parentGUID})")
    void putTreeNode(@Param("guid") GUID guid,@Param("parentGUID") GUID parentGUID);

    default GUIDDistributedTrieNode getNode(@Param("guid") GUID guid) {
        GUIDDistributedTrieNode nodeMeta = this.getNodeMeta( guid );
        if( nodeMeta != null ) {
            List<GUID > parentNode = this.getParentNodes( guid );
            if ( parentNode != null ){
                nodeMeta.setParentGUID( parentNode );
            }
        }
        return nodeMeta;
    }

    @Select("SELECT parent_guid FROM hydra_service_node_tree WHERE guid = #{guid}" )
    List<GUID> getParentNodes(@Param("guid") GUID guid);

    @Select("SELECT `guid`, `base_data_guid` AS baseDataGUID, `node_metadata_guid` AS nodeMetadataGUID, `type` FROM `hydra_service_meta_map` WHERE `guid`=#{guid}")
    GUIDDistributedTrieNode getNodeMeta(@Param("guid") GUID guid);

    default void remove(@Param("guid") GUID guid){
        removeNodeMeta(guid);
        removeTreeNode(guid);
    }

    @Delete("DELETE FROM `hydra_service_meta_map` WHERE `guid` = #{guid}")
    void removeNodeMeta(@Param("guid") GUID guid);

    @Delete("DELETE FROM `hydra_service_node_tree` WHERE `guid` = #{guid}")
    void removeTreeNode(@Param("guid") GUID guid);

    @Update("UPDATE `hydra_service_node_path` SET `path`=#{Path} WHERE `guid` = #{guid}")
    void updatePath(@Param("guid") GUID guid, @Param("Path") String path);

    @Select("SELECT `path` FROM `hydra_service_node_path`  WHERE `guid` = #{guid}")
    String getPath(@Param("guid") GUID guid);

    @Insert("INSERT INTO `hydra_service_node_path` (`guid`, `path`) VALUES (#{guid},#{path})")
    void putPath(@Param("path") String path,@Param("guid") GUID guid );


    @Select("SELECT `guid` FROM `hydra_service_node_path` WHERE `path`=#{path}")
    GUID queryGUIDByPath(@Param("path") String path);

    @Insert("INSERT INTO hydra_service_node_tree SET guid=#{nodeGUID}, parent_guid=#{parentGUID}")
    void insertNodeToParent(@Param("nodeGUID") GUID nodeGUID,@Param("parentGUID") GUID parentGUID);


    @Select("SELECT hsnt.`guid` , `parent_guid` AS parentGUID, `base_data_guid` AS baseDataGUID, `node_metadata_guid` AS nodeMetadataGUID, type FROM `hydra_service_node_tree` hsnt,hydra_service_meta_map hsmm WHERE `parent_guid`=#{guid} AND hsmm.guid=hsnt.guid")
    List<GUIDDistributedTrieNode> getChild(GUID guid);

    @Delete("DELETE FROM `hydra_service_node_path` WHERE `guid`=#{guid}")
    void removePath(GUID guid);

    @Update("UPDATE `hydra_service_node_tree` hsnt,`hydra_service_meta_map` hsmm set `parent_guid`=#{node.parentGUID},`base_data_guid`=#{node.baseDataGUID},`node_metadata_guid`=#{node.nodeMetadataGUID} WHERE `guid`=#{guid} AND hsmm.guid=hsnt.guid")
    void putNode(@Param("guid") GUID guid,@Param("node") GUIDDistributedTrieNode distributedTreeNode);

    @Select(" SELECT COUNT(*) FROM `hydra_service_node_tree` ")
    long size();

    @Delete("DELETE FROM hydra_service_node_tree WHERE guid = #{childGUID} AND parent_guid = #{parentGUID}")
    void removeInheritance(@Param("childGUID") GUID childNode, @Param("parentGUID") GUID parentGUID);
}
