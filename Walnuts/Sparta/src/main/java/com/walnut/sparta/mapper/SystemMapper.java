package com.walnut.sparta.mapper;

import com.walnut.sparta.entity.ApplicationDescription;
import com.walnut.sparta.entity.ApplicationNode;
import com.walnut.sparta.entity.ClassificationNode;
import com.walnut.sparta.entity.ClassificationRules;
import com.walnut.sparta.entity.Node;
import com.walnut.sparta.entity.NodeMetadata;
import com.walnut.sparta.entity.ServiceDescription;
import com.walnut.sparta.entity.ServiceNode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.nio.file.Path;
import java.util.List;


@Mapper
public interface SystemMapper {
    @Insert("INSERT INTO  `hydra_node` (`uuid`, parentUUID, baseDataUUID, nodeMetadataUUID,`type`) VALUES (#{UUID},#{parentUUID},#{baseDataUUID},#{nodeMetadataUUID},#{type})")
    void saveNode(Node node);
    @Select("SELECT `id`, `uuid`, parentUUID, baseDataUUID, nodeMetadataUUID ,`type` FROM `hydra_node` where uuid=#{uuid}")
    Node selectNodeUUID( @Param("uuid") String uuid);
    @Delete("DELETE FROM `hydra_node` WHERE uuid=#{uuid}")
    void deleteNode(@Param("uuid") String uuid);
    @Insert("INSERT INTO  `hydra_application_node` (`uuid`, `name`) VALUES (#{UUID},#{name})")
    void saveApplicationNode(ApplicationNode applicationNode);
    @Insert("INSERT INTO `hydra_application_description` (UUID, `name`, `path`, `type`, `alias`, resourceType, deploymentMethod, createTime, updateTime) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{deploymentMethod},#{createTime},#{updateTime})")
    void saveApplicationDescription(ApplicationDescription applicationDescription);
    @Insert("INSERT INTO `hydra_node_metadata` (UUID, `scenario`, primaryImplLang, extraInformation, `level`, `description`) VALUES (#{UUID},#{scenario},#{primaryImplLang},#{extraInformation},#{level},#{description})")
    void saveNodeMetadata(NodeMetadata nodeMetadata);
    @Insert("INSERT INTO `hydra_service_node` (`uuid`, `name`) VALUES (#{UUID},#{name})")
    void saveServiceNode(ServiceNode serviceNode);
    @Insert("INSERT INTO `hydra_service_description` (UUID, `name`, `path`, `type`, `alias`, resourceType, serviceType, createTime, updateTime) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{serviceType},#{createTime},#{updateTime})")
    void saveServiceDescription(ServiceDescription serviceDescription);
    @Insert("INSERT INTO `hydra_classif_node` (`uuid`, `name`, `rulesUUID`) VALUES (#{UUID},#{name},#{rulesUUID})")
    void saveClassifNode(ClassificationNode classificationNode);
    @Insert("INSERT INTO `hydra_classif_rules` (uuid, scope, name, description) VALUES (#{UUID},#{scope},#{name},#{description})")
    void saveClassifRules(ClassificationRules classificationRules);
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
     Node getPathInformation(String UUID);
     @Select("SELECT `path` FROM `hydra_node_path` WHERE UUID=#{UUID}")
     String selectPath(String UUID);
     @Insert("INSERT INTO `hydra_node_path` (UUID, `path`) VALUES (#{UUID},#{path})")
     void savePath(@Param("path") String path,@Param("UUID") String UUID);
     @Select("SELECT `id`, `uuid`, `name` FROM `hydra_application_node` WHERE `uuid`=#{UUID}")
     ApplicationNode selectApplicationNode(String UUID);
     @Select("SELECT `id`, UUID, `name`, `path`, `type`, `alias`, resourceType, deploymentMethod, createTime, updateTime FROM `hydra_application_description` WHERE UUID=#{UUID}")
     ApplicationDescription selectApplicationDescription(String UUID);
     @Select("SELECT `id`, UUID, `scenario`, primaryImplLang, extraInformation, `level`, `description` FROM `hydra_node_metadata` WHERE UUID=#{UUID}")
     NodeMetadata selectNodeMetadata(String UUID);
     @Select("SELECT `id`, `uuid`, `name` FROM `hydra_service_node` WHERE `uuid`=#{UUID}")
     ServiceNode selectServiceNode(String UUID);
     @Select("SELECT `id`, UUID, `name`, `path`, `type`, `alias`, resourceType, serviceType, createTime, updateTime FROM `hydra_service_description` WHERE UUID=#{UUID}")
     ServiceDescription selectServiceDescription(String UUID);
     @Select("SELECT `id`, `uuid`, `scope`, `name`, `description` FROM `hydra_classif_rules` WHERE `uuid`=#{UUID}")
     ClassificationRules selectClassificationRules(String UUID);
     @Select("SELECT `id`, `uuid`, `name`, `rulesUUID` FROM `hydra_classif_node` WHERE `uuid`=#{UUID}")
     ClassificationNode selectClassificationNode(String UUID);
     @Select("SELECT `id`, `UUID`, `parentUUID`, `baseDataUUID`, `nodeMetadataUUID`, `type` FROM `hydra_node` WHERE `parentUUID`=#{UUID}")
     List<Node> selectChildNode(@Param("UUID") String UUID);
     @Update("UPDATE `hydra_node` SET `parentUUID`=#{UUID} WHERE `UUID`=#{UUID}")
     void updateNode(Node node);
     @Update("UPDATE `hydra_node_path` SET `path`=#{Path} WHERE `UUID`=#{UUID}")
     void updatePath(@Param("UUID") String UUID, @Param("Path") String path);
}
