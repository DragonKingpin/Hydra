package com.walnut.sparta.mapper;

import com.walnut.sparta.ServiceTree.Interface.ServiceTreeDao;
import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.service.GenericApplicationNode;
import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.unit.udsn.UUIDDistributedScopeNode;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.service.GenericServiceDescription;
import com.pinecone.hydra.service.GenericServiceNode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface SystemMapper extends ServiceTreeDao {
    @Insert("INSERT INTO  `hydra_service_node_tree` (`uuid`, parentUUID, baseDataUUID, nodeMetadataUUID,`type`) VALUES (#{UUID},#{parentUUID},#{baseDataUUID},#{nodeMetadataUUID},#{type})")
    void saveNode(UUIDDistributedScopeNode node);
    @Select("SELECT `id`, `uuid`, parentUUID, baseDataUUID, nodeMetadataUUID ,`type` FROM `hydra_service_node_tree` where uuid=#{uuid}")
    UUIDDistributedScopeNode selectNode(@Param("uuid") String uuid);
    @Delete("DELETE FROM `hydra_service_node_tree` WHERE uuid=#{uuid}")
    void deleteNode(@Param("uuid") String uuid);
    @Insert("INSERT INTO  `hydra_application_node` (`uuid`, `name`) VALUES (#{UUID},#{name})")
    void saveApplicationNode(GenericApplicationNode applicationNode);
    @Insert("INSERT INTO `hydra_application_description` (UUID, `name`, `path`, `type`, `alias`, resourceType, deploymentMethod, createTime, updateTime) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{deploymentMethod},#{createTime},#{updateTime})")
    void saveApplicationDescription(GenericApplicationDescription applicationDescription);
    @Insert("INSERT INTO `hydra_node_metadata` (UUID, `scenario`, primaryImplLang, extraInformation, `level`, `description`) VALUES (#{UUID},#{scenario},#{primaryImplLang},#{extraInformation},#{level},#{description})")
    void saveNodeMetadata(GenericNodeMetadata nodeMetadata);
    @Insert("INSERT INTO `hydra_service_node` (`uuid`, `name`) VALUES (#{UUID},#{name})")
    void saveServiceNode(GenericServiceNode serviceNode);
    @Insert("INSERT INTO `hydra_service_description` (UUID, `name`, `path`, `type`, `alias`, resourceType, serviceType, createTime, updateTime) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{serviceType},#{createTime},#{updateTime})")
    void saveServiceDescription(GenericServiceDescription serviceDescription);
    @Insert("INSERT INTO `hydra_classif_node` (`uuid`, `name`, `rulesUUID`) VALUES (#{UUID},#{name},#{rulesUUID})")
    void saveClassifNode(GenericClassificationNode classificationNode);
    @Insert("INSERT INTO `hydra_classif_rules` (uuid, scope, name, description) VALUES (#{UUID},#{scope},#{name},#{description})")
    void saveClassifRules(GenericClassificationRules classificationRules);
    @Delete("DELETE FROM `hydra_application_node` WHERE `uuid`=#{UUID}")
    void deleteApplicationNode(String UUID);
    @Delete("DELETE FROM `hydra_application_description` WHERE UUID=#{UUID}")
    void deleteApplicationDescription(String UUID);
    @Delete("DELETE FROM `hydra_node_metadata` WHERE UUID=#{UUID}")
    void deleteNodeMetadata(String UUID);
    @Delete("DELETE FROM `hydra_service_node` WHERE `uuid`=#{UUID}")
    void deleteServiceNode(String UUID);
    @Delete("DELETE FROM `hydra_service_description` WHERE UUID=#{UUID}")
    void deleteServiceDescription(String UUID);
    @Delete("DELETE FROM `hydra_classif_node` WHERE `uuid`=#{UUID}")
    void deleteClassifNode(String UUID);
    @Delete("DELETE FROM `hydra_classif_rules` WHERE `uuid`=#{UUID}")
    void deleteClassifRules(String UUID);
     UUIDDistributedScopeNode getPathInformation(String UUID);
     @Select("SELECT `path` FROM `hydra_node_path` WHERE UUID=#{UUID}")
     String selectPath(String UUID);
     @Insert("INSERT INTO `hydra_node_path` (UUID, `path`) VALUES (#{UUID},#{path})")
     void savePath(@Param("path") String path,@Param("UUID") String UUID);
     @Select("SELECT `id`, `uuid`, `name` FROM `hydra_application_node` WHERE `uuid`=#{UUID}")
     GenericApplicationNode selectApplicationNode(String UUID);
     @Select("SELECT `id`, UUID, `name`, `path`, `type`, `alias`, resourceType, deploymentMethod, createTime, updateTime FROM `hydra_application_description` WHERE UUID=#{UUID}")
     GenericApplicationDescription selectApplicationDescription(String UUID);
     @Select("SELECT `id`, UUID, `scenario`, primaryImplLang, extraInformation, `level`, `description` FROM `hydra_node_metadata` WHERE UUID=#{UUID}")
     GenericNodeMetadata selectNodeMetadata(String UUID);
     @Select("SELECT `id`, `uuid`, `name` FROM `hydra_service_node` WHERE `uuid`=#{UUID}")
     GenericServiceNode selectServiceNode(String UUID);
     @Select("SELECT `id`, UUID, `name`, `path`, `type`, `alias`, resourceType, serviceType, createTime, updateTime FROM `hydra_service_description` WHERE UUID=#{UUID}")
     GenericServiceDescription selectServiceDescription(String UUID);
     @Select("SELECT `id`, `uuid`, `scope`, `name`, `description` FROM `hydra_classif_rules` WHERE `uuid`=#{UUID}")
     GenericClassificationRules selectClassifRules(String UUID);
     @Select("SELECT `id`, `uuid`, `name`, `rulesUUID` FROM `hydra_classif_node` WHERE `uuid`=#{UUID}")
     GenericClassificationNode selectClassifNode(String UUID);
     @Select("SELECT `id`, `UUID`, `parentUUID`, `baseDataUUID`, `nodeMetadataUUID`, `type` FROM `hydra_service_node_tree` WHERE `parentUUID`=#{UUID}")
     List<UUIDDistributedScopeNode> selectChildNode(@Param("UUID") String UUID);
     @Update("UPDATE `hydra_service_node_tree` SET `parentUUID`=#{UUID} WHERE `UUID`=#{UUID}")
     void updateNode(UUIDDistributedScopeNode node);
     @Update("UPDATE `hydra_node_path` SET `path`=#{Path} WHERE `UUID`=#{UUID}")
     void updatePath(@Param("UUID") String UUID, @Param("Path") String path);
}
