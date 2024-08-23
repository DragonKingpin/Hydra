package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericServiceDescription;
import com.pinecone.hydra.service.tree.source.ServiceDescriptionManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface GenericServiceDescriptionManipinate extends ServiceDescriptionManipulator {
    @Insert("INSERT INTO `hydra_service_description` (`guid`, `name`, `path`, `type`, `alias`, resource_type, service_type, create_time, update_time) VALUES (#{UUID},#{name},#{path},#{type},#{alias},#{resourceType},#{serviceType},#{createTime},#{updateTime})")
    void saveServiceDescription(GenericServiceDescription serviceDescription);

    @Delete("DELETE FROM `hydra_service_description` WHERE `guid`=#{UUID}")
    void deleteServiceDescription(@Param("UUID")GUID UUID);

    void updateServiceDescription(GenericServiceDescription serviceDescription);

    @Select("SELECT `id`, `guid` AS UUID, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `service_type` AS serviceType, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_service_description` WHERE `guid`=#{UUID}")
    GenericServiceDescription selectServiceDescription(@Param("UUID")GUID UUID);
}
