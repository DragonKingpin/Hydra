package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ScopeTreeMapper extends ScopeTreeManipulator {
    default void saveNode(GUIDDistributedScopeNode node){
        this.putNodeMeta(node);
        if ( !node.getParentGUIDs().isEmpty() ){
            for ( GUID guid:node.getParentGUIDs() ){
                this.putTreeNode( node.getGuid(), guid );
            }
        }
    }

    @Insert("INSERT INTO `hydra_service_meta_map` (`guid`, `base_data_guid`, `node_metadata_guid`, `type`) VALUES (#{guid},#{baseDataGUID},#{nodeMetadataGUID},#{type})")
    void putNodeMeta(GUIDDistributedScopeNode node);

    @Insert("INSERT INTO `hydra_service_node_tree` (`guid`, `parent_guid`) VALUES (#{guid},#{parentGUID})")
    void putTreeNode(@Param("guid") GUID guid,@Param("parentGUID") GUID parentGUID);

    default GUIDDistributedScopeNode getNode(@Param("guid") GUID guid) {
        GUIDDistributedScopeNode nodeMeta = this.getNodeMeta( guid );
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
    GUIDDistributedScopeNode getNodeMeta(@Param("guid") GUID guid);

    default void removeNode(@Param("guid") GUID guid){
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
    GUID parsePath(@Param("path") String path);

    @Insert("INSERT INTO hydra_service_node_tree SET guid=#{nodeGUID}, parent_guid=#{parentGUID}")
    void insertNodeToParent(@Param("nodeGUID") GUID nodeGUID,@Param("parentGUID") GUID parentGUID);

    @Select("SELECT hcr.name FROM `hydra_service_classif_node_rules` hcnr,`hydra_service_classif_rules` hcr WHERE hcnr.classif_rule_guid=hcr.guid AND hcnr.classif_node_guid = #{classifNodeGUID}")
    String getClassifNodeClassif(GUID classifNodeGUID);

    @Select("SELECT hsnt.`guid` , `parent_guid` AS parentGUID, `base_data_guid` AS baseDataGUID, `node_metadata_guid` AS nodeMetadataGUID, type FROM `hydra_service_node_tree` hsnt,hydra_service_meta_map hsmm WHERE `parent_guid`=#{guid} AND hsmm.guid=hsnt.guid")
    List<GUIDDistributedScopeNode > getChildNode(GUID guid);

    @Delete("DELETE FROM `hydra_service_node_path` WHERE `guid`=#{guid}")
    void removePath(GUID guid);

    @Update("UPDATE `hydra_service_node_tree` hsnt,`hydra_service_meta_map` hsmm set `parent_guid`=#{node.parentGUID},`base_data_guid`=#{node.baseDataGUID},`node_metadata_guid`=#{node.nodeMetadataGUID} WHERE `guid`=#{guid} AND hsmm.guid=hsnt.guid")
    void putNode(@Param("guid") GUID guid,@Param("node") GUIDDistributedScopeNode distributedTreeNode);

    @Select(" SELECT COUNT(*) FROM `hydra_service_node_tree` ")
    long size();

    @Delete("DELETE FROM hydra_service_node_tree WHERE guid = #{childGUID} AND parent_guid = #{parentGUID}")
    void removeInheritance(@Param("childGUID") GUID childNode, @Param("parentGUID") GUID parentGUID);
}
