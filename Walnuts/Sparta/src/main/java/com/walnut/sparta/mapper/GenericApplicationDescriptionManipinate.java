package com.walnut.sparta.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.unit.udsn.ApplicationDescriptionManipinate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface GenericApplicationDescriptionManipinate extends ApplicationDescriptionManipinate {
    @Insert("INSERT INTO `hydra_application_description` (UUID, `name`, `path`, `type`, `alias`, resource_type, deployment_method, create_time, update_time) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{deploymentMethod},#{createTime},#{updateTime})")
    void saveApplicationDescription(GenericApplicationDescription applicationDescription);
    @Delete("DELETE FROM `hydra_application_description` WHERE UUID=#{UUID}")
    void deleteApplicationDescription(@Param("UUID")GUID UUID);
    @Select("SELECT `id`, UUID, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `deployment_method` AS deploymentMethod, `create_time` AS createTime, `create_time` AS createTime FROM `hydra_application_description` WHERE UUID=#{UUID}")
    GenericApplicationDescription selectApplicationDescription(@Param("UUID")GUID UUID);
    void updateApplicationDescription(GenericApplicationDescription applicationDescription);
}
