package com.walnut.sparta.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.UUID;

@Mapper
public interface TreeMapper extends ServiceTreeMapper {
    @Insert("INSERT INTO  `hydra_service_node_tree` (`uuid`, `parent_uuid`, `base_data_uuid`, `node_metadata_uuid`,`type`) VALUES (#{UUID},#{parentUUID},#{baseDataUUID},#{nodeMetadataUUID},#{type})")
    void saveNode(GUIDDistributedScopeNode node);
    @Select("SELECT `id`, `uuid`, `parent_uuid` AS parentUUID, `base_data_uuid` AS baseDataUUID, `node_metadata_uuid` AS nodeMetadataUUID,`type` FROM `hydra_service_node_tree` where uuid=#{uuid}")
    GUIDDistributedScopeNode selectNode(@Param("uuid") GUID uuid);
    @Delete("DELETE FROM `hydra_service_node_tree` WHERE uuid=#{uuid}")
    void deleteNode(@Param("uuid") GUID uuid);
    @Update("UPDATE `hydra_service_node_tree` SET parent_uuid=#{UUID} WHERE `UUID`=#{UUID}")
    void updateNode(GUIDDistributedScopeNode node);
    @Update("UPDATE `hydra_node_path` SET `path`=#{Path} WHERE `UUID`=#{UUID}")
    void updatePath(@Param("UUID") GUID UUID, @Param("Path") String path);
    @Select("SELECT `path` FROM `hydra_node_path` WHERE UUID=#{UUID}")
    String selectPath(@Param("UUID") GUID UUID);
    @Insert("INSERT INTO `hydra_node_path` (UUID, `path`) VALUES (#{UUID},#{path})")
    void savePath(@Param("path") String path,@Param("UUID") GUID UUID);
    @Select("SELECT `id`, `UUID`, `parent_uuid`, `base_data_uuid`, `node_metadata_uuid`, `type` FROM `hydra_service_node_tree` WHERE `parent_uuid`=#{UUID}")
    List<GUIDDistributedScopeNode> selectChildNode(@Param("UUID")GUID UUID);
    @Select("SELECT UUID FROM hydra_node_path WHERE path=#{path}")
    GUID parsePath(@Param("path") String path);
}
