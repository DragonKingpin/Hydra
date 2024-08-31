package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.tree.source.ServiceMetaManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface GenericServiceMetaMapper extends ServiceMetaManipulator {
    @Insert("INSERT INTO `hydra_service_serv_node_meta` (`guid`, `name`, `path`, `type`, `alias`, resource_type, service_type, create_time, update_time) VALUES (#{guid},#{name},#{path},#{type},#{alias},#{resourceType},#{serviceType},#{createTime},#{updateTime})")
    void insert(GenericServiceNodeMeta serviceDescription);

    @Delete("DELETE FROM `hydra_service_serv_node_meta` WHERE `guid`=#{guid}")
    void remove(@Param("guid")GUID guid);

    void update(GenericServiceNodeMeta serviceDescription);

    @Select("SELECT `id`, `guid`, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `service_type` AS serviceType, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_service_serv_node_meta` WHERE `guid`=#{guid}")
    GenericServiceNodeMeta getServiceMeta(@Param("guid")GUID guid);
}
