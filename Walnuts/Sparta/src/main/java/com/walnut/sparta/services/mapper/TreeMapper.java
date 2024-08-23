package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TreeMapper extends ServiceTreeMapper {
    @Insert("INSERT INTO  `hydra_service_node_tree` (`guid`, `parent_guid`, `base_data_guid`, `node_metadata_guid`,`type`) VALUES (#{UUID},#{parentUUID},#{baseDataUUID},#{nodeMetadataUUID},#{type})")
    void saveNode(GUIDDistributedScopeNode node);

    @Select("SELECT `id`, `guid` AS UUID, `parent_guid` AS parentUUID, `base_data_guid` AS baseDataUUID, `node_metadata_guid` AS nodeMetadataUUID,`type` FROM `hydra_service_node_tree` where guid=#{uuid}")
    GUIDDistributedScopeNode selectNode(@Param("uuid") GUID uuid);

    @Delete("DELETE FROM `hydra_service_node_tree` WHERE `guid`=#{uuid}")
    void deleteNode(@Param("uuid") GUID uuid);

    @Update("UPDATE `hydra_service_node_tree` SET `parent_guid`=#{UUID} WHERE `guid`=#{UUID}")
    void updateNode(GUIDDistributedScopeNode node);

    @Update("UPDATE `hydra_node_path` SET `path`=#{Path} WHERE `guid`=#{UUID}")
    void updatePath(@Param("UUID") GUID UUID, @Param("Path") String path);

    @Select("SELECT `path` FROM `hydra_node_path` WHERE `guid`=#{UUID}")
    String selectPath(@Param("UUID") GUID UUID);

    @Insert("INSERT INTO `hydra_node_path` (`guid`, `path`) VALUES (#{UUID},#{path})")
    void savePath(@Param("path") String path,@Param("UUID") GUID UUID);

    @Select("SELECT `id`, `guid` AS UUID, `parent_guid` AS parentUUID, `base_data_guid` AS baseDataUUID, `node_metadata_guid` AS nodeMetadataUUID, `type` FROM `hydra_service_node_tree` WHERE `parent_guid`=#{UUID}")
    List<GUIDDistributedScopeNode> selectChildNode(@Param("UUID")GUID UUID);

    @Select("SELECT `guid` FROM `hydra_node_path` WHERE `path`=#{path}")
    GUID parsePath(@Param("path") String path);

    @Update("UPDATE `hydra_service_node_tree` SET `parent_guid`=#{parentGUID} WHERE `guid`=#{nodeGUID}")
    void addNodeToParent(@Param("nodeGUID") GUID nodeGUID,@Param("parentGUID") GUID parentGUID);

    @Select("SELECT hcr.name FROM hydra_classif_node_rules hcnr,hydra_classif_rules hcr WHERE hcnr.classif_rule_guid=hcr.guid AND hcnr.classif_node_guid=#{classifNodeGUID}")
    String getClassifNodeClassif(GUID classifNodeGUID);

    @Select("SELECT `id`, `guid` AS UUID, `parent_guid` AS parentUUID, `base_data_guid` AS baseDataUUID, `node_metadata_guid` AS nodeMetadataUUID, type FROM `hydra_service_node_tree` WHERE `parent_guid`=#{guid}")
    List<GUIDDistributedScopeNode> getChildNode(GUID guid);

    @Delete("DELETE FROM `hydra_node_path` WHERE `guid`=#{guid}")
    void deletePath(GUID guid);
}
