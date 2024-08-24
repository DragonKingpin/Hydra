package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericApplicationNodeMetadata;
import com.pinecone.hydra.service.tree.source.ApplicationDescriptionManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface GenericApplicationDescriptionManipulator extends ApplicationDescriptionManipulator {
    @Insert("INSERT INTO `hydra_application_description` (`guid`, `name`, `path`, `type`, `alias`, resource_type, deployment_method, create_time, update_time) VALUES (#{guid},#{name},#{path},#{type},#{alias},#{resourceType},#{deploymentMethod},#{createTime},#{updateTime})")
    void saveApplicationDescription(GenericApplicationNodeMetadata applicationDescription);

    @Delete("DELETE FROM `hydra_application_description` WHERE `guid`=#{guid}")
    void deleteApplicationDescription(@Param("guid")GUID guid);

    @Select("SELECT `id`, `guid`, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `deployment_method` AS deploymentMethod, `create_time` AS createTime, `create_time` AS createTime FROM `hydra_application_description` WHERE `guid`=#{guid}")
    GenericApplicationNodeMetadata selectApplicationDescription(@Param("guid")GUID guid );

    void updateApplicationDescription( GenericApplicationNodeMetadata applicationDescription );
}
