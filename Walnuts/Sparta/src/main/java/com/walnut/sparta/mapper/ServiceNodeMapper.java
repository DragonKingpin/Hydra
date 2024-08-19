package com.walnut.sparta.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.DistrubuteScopeTreeDataManipinate;
import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.service.GenericApplicationNode;
import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
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
import java.util.UUID;


@Mapper
public interface ServiceNodeMapper extends DistrubuteScopeTreeDataManipinate {

    @Insert("INSERT INTO  `hydra_application_node` (`uuid`, `name`) VALUES (#{UUID},#{name})")
    void saveApplicationNode(GenericApplicationNode applicationNode);
    @Insert("INSERT INTO `hydra_application_description` (UUID, `name`, `path`, `type`, `alias`, resource_type, deployment_method, create_time, update_time) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{deploymentMethod},#{createTime},#{updateTime})")
    void saveApplicationDescription(GenericApplicationDescription applicationDescription);
    @Insert("INSERT INTO `hydra_node_metadata` (UUID, `scenario`, primary_impl_lang, extra_information, `level`, `description`) VALUES (#{UUID},#{scenario},#{primaryImplLang},#{extraInformation},#{level},#{description})")
    void saveNodeMetadata(GenericNodeMetadata nodeMetadata);
    @Insert("INSERT INTO `hydra_service_node` (`uuid`, `name`) VALUES (#{UUID},#{name})")
    void saveServiceNode(GenericServiceNode serviceNode);
    @Insert("INSERT INTO `hydra_service_description` (UUID, `name`, `path`, `type`, `alias`, resource_type, service_type, create_time, update_time) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{serviceType},#{createTime},#{updateTime})")
    void saveServiceDescription(GenericServiceDescription serviceDescription);
    @Insert("INSERT INTO `hydra_classif_node` (`uuid`, `name`, `rules_uuid`) VALUES (#{UUID},#{name},#{rulesUUID})")
    void saveClassifNode(GenericClassificationNode classificationNode);
    @Insert("INSERT INTO `hydra_classif_rules` (uuid, scope, name, description) VALUES (#{UUID},#{scope},#{name},#{description})")
    void saveClassifRules(GenericClassificationRules classificationRules);
    @Delete("DELETE FROM `hydra_application_node` WHERE `uuid`=#{UUID}")
    void deleteApplicationNode(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_application_description` WHERE UUID=#{UUID}")
    void deleteApplicationDescription(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_node_metadata` WHERE UUID=#{UUID}")
    void deleteNodeMetadata(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_service_node` WHERE `uuid`=#{UUID}")
    void deleteServiceNode(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_service_description` WHERE UUID=#{UUID}")
    void deleteServiceDescription(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_classif_node` WHERE `uuid`=#{UUID}")
    void deleteClassifNode(@Param("UUID")GUID UUID);
    @Delete("DELETE FROM `hydra_classif_rules` WHERE `uuid`=#{UUID}")
    void deleteClassifRules(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `uuid`, `name` FROM `hydra_application_node` WHERE `uuid`=#{UUID}")
     GenericApplicationNode selectApplicationNode(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, UUID, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `deployment_method` AS deploymentMethod, `create_time` AS createTime, `create_time` AS createTime FROM `hydra_application_description` WHERE UUID=#{UUID}")
     GenericApplicationDescription selectApplicationDescription(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, UUID, `scenario`, `primary_impl_lang` AS primaryImplLang, `extra_information` AS extraInformation, `level`, `description` FROM `hydra_node_metadata` WHERE UUID=#{UUID}")
     GenericNodeMetadata selectNodeMetadata(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `uuid`, `name` FROM `hydra_service_node` WHERE `uuid`=#{UUID}")
     GenericServiceNode selectServiceNode(@Param("UUID") GUID UUID);
     @Select("SELECT `id`, UUID, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `service_type` AS serviceType, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_service_description` WHERE UUID=#{UUID}")
     GenericServiceDescription selectServiceDescription(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `uuid`, `scope`, `name`, `description` FROM `hydra_classif_rules` WHERE `uuid`=#{UUID}")
     GenericClassificationRules selectClassifRules(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `uuid`, `name`, `rules_uuid` AS rulesUUID FROM `hydra_classif_node` WHERE `uuid`=#{UUID}")
     GenericClassificationNode selectClassifNode(@Param("UUID")GUID UUID);
     @Select("SELECT `id`, `UUID`, `name` FROM `hydra_service_node` WHERE name=#{name}")
     List<GenericServiceNode> selectServiceNodeByName(@Param("name") String name);
     @Select("SELECT `id`, `UUID`, `name`, `rules_uuid` FROM `hydra_classif_node` WHERE name=#{name}")
     List<GenericClassificationNode> selectClassifNodeByName(@Param("name") String name);
     @Select("SELECT `id`, `UUID`, `name` FROM `hydra_application_node` WHERE name=#{name}")
     List<GenericApplicationNode> selectApplicationNodeByName(@Param("name") String name);
     @Insert("INSERT INTO `hydra_classif_node_rules` (`classif_node_uuid`, `classif_rule_uuid`) VALUES (#{classifNodeUUID},#{classifTypeUUID})")
     void saveClassifNodeType(@Param("classifNodeUUID") GUID classifNodeUUID,@Param("classifTypeUUID") GUID classifTypeUUID);
     @Select("SELECT `classif_rule_uuid` FROM `hydra_classif_node_rules`")
     GUID selectClassifNodeType(GUID classifNodeUUID);
}
