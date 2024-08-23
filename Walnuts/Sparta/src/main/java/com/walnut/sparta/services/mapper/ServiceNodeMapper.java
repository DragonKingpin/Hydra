package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.source.DistrubuteScopeTreeDataManipulator;
import com.pinecone.hydra.service.tree.GenericApplicationDescription;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeMetadata;
import com.pinecone.hydra.service.tree.GenericServiceDescription;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface ServiceNodeMapper extends DistrubuteScopeTreeDataManipulator {

    @Insert("INSERT INTO  `hydra_application_node` (`guid`, `name`) VALUES (#{UUID},#{name})")
    void saveApplicationNode(GenericApplicationNode applicationNode);
    @Insert("INSERT INTO `hydra_application_description` (guid, `name`, `path`, `type`, `alias`, resource_type, deployment_method, create_time, update_time) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{deploymentMethod},#{createTime},#{updateTime})")
    void saveApplicationDescription(GenericApplicationDescription applicationDescription);
    @Insert("INSERT INTO `hydra_node_metadata` (guid, `scenario`, primary_impl_lang, extra_information, `level`, `description`) VALUES (#{UUID},#{scenario},#{primaryImplLang},#{extraInformation},#{level},#{description})")
    void saveNodeMetadata(GenericNodeMetadata nodeMetadata);
    @Insert("INSERT INTO `hydra_service_node` (guid, `name`) VALUES (#{UUID},#{name})")
    void saveServiceNode(GenericServiceNode serviceNode);
    @Insert("INSERT INTO `hydra_service_description` (guid, `name`, `path`, `type`, `alias`, resource_type, service_type, create_time, update_time) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{serviceType},#{createTime},#{updateTime})")
    void saveServiceDescription(GenericServiceDescription serviceDescription);
    @Insert("INSERT INTO `hydra_classif_node` (`guid`, `name`, `rules_guid`) VALUES (#{UUID},#{name},#{rulesUUID})")
    void saveClassifNode(GenericClassificationNode classificationNode);
    @Insert("INSERT INTO `hydra_classif_rules` (guid, scope, name, description) VALUES (#{UUID},#{scope},#{name},#{description})")
    void saveClassifRules(GenericClassificationRules classificationRules);
    @Delete("DELETE FROM `hydra_application_node` WHERE `guid`=#{UUID}")
    void deleteApplicationNode(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_application_description` WHERE guid=#{UUID}")
    void deleteApplicationDescription(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_node_metadata` WHERE guid=#{UUID}")
    void deleteNodeMetadata(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_service_node` WHERE guid=#{UUID}")
    void deleteServiceNode(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_service_description` WHERE guid=#{UUID}")
    void deleteServiceDescription(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_classif_node` WHERE `guid`=#{UUID}")
    void deleteClassifNode(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_classif_rules` WHERE guid=#{UUID}")
    void deleteClassifRules(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `guid` AS UUID, `name` FROM `hydra_application_node` WHERE `guid`=#{UUID}")
     GenericApplicationNode selectApplicationNode(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `guid` AS UUID, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `deployment_method` AS deploymentMethod, `create_time` AS createTime, `create_time` AS createTime FROM `hydra_application_description` WHERE guid=#{UUID}")
     GenericApplicationDescription selectApplicationDescription(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `guid` AS UUID, `scenario`, `primary_impl_lang` AS primaryImplLang, `extra_information` AS extraInformation, `level`, `description` FROM `hydra_node_metadata` WHERE guid=#{UUID}")
     GenericNodeMetadata selectNodeMetadata(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `guid` AS UUID, `name` FROM `hydra_service_node` WHERE guid=#{UUID}")
     GenericServiceNode selectServiceNode(@Param("UUID") GUID UUID);
     @Select("SELECT `id`, `guid` AS UUID, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `service_type` AS serviceType, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_service_description` WHERE guid=#{UUID}")
     GenericServiceDescription selectServiceDescription(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `guid` AS UUID, `scope`, `name`, `description` FROM `hydra_classif_rules` WHERE guid=#{UUID}")
     GenericClassificationRules selectClassifRules(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `guid` AS UUID, `name`, `rules_guid` AS rulesUUID FROM `hydra_classif_node` WHERE `guid`=#{UUID}")
     GenericClassificationNode selectClassifNode(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `guid` AS UUID, `name` FROM `hydra_service_node` WHERE name=#{name}")
     List<GenericServiceNode> selectServiceNodeByName(@Param("name") String name);
     @Select("SELECT `id`, `guid` AS UUID, `name`, `rules_guid` AS rulesUUID FROM `hydra_classif_node` WHERE name=#{name}")
     List<GenericClassificationNode> selectClassifNodeByName(@Param("name") String name);
     @Select("SELECT `id`, `guid` AS UUID, `name` FROM `hydra_application_node` WHERE name=#{name}")
     List<GenericApplicationNode> selectApplicationNodeByName(@Param("name") String name);
     @Insert("INSERT INTO `hydra_classif_node_rules` (classif_node_guid, classif_rule_guid) VALUES (#{classifNodeUUID},#{classifTypeUUID})")
     void saveClassifNodeType(@Param("classifNodeUUID") GUID classifNodeUUID,@Param("classifTypeUUID") GUID classifTypeUUID);
     @Select("SELECT classif_rule_guid  FROM `hydra_classif_node_rules`")
     GUID selectClassifNodeType(GUID classifNodeUUID);
}
