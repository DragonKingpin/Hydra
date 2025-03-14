package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.TaskMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
@IbatisDataAccessObject
public interface TaskMetaMapper extends TaskMetaManipulator {
    @Insert("INSERT INTO `hydra_task_serv_node_meta` (`guid`, `name`, `path`, `type`, `alias`, resource_type, service_type, create_time, update_time) VALUES (#{metaGuid},#{name},#{path},#{type},#{alias},#{resourceType},#{serviceType},#{createTime},#{updateTime})")
    void insert( TaskElement taskElement );

    @Delete("DELETE FROM `hydra_task_serv_node_meta` WHERE `guid`=#{guid}")
    void remove( @Param("guid") GUID guid );


    @Select("SELECT `id` AS `enumId`, `guid`, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `service_type` AS serviceType, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_service_serv_node_meta` WHERE `guid`=#{guid}")
    GenericTaskElement getTaskMeta(@Param("guid") GUID guid );
    @Update("UPDATE `hydra_task_serv_node_meta` SET `name` =#{name}, `path` = #{path}, `type` = #{type}, `alias` = #{alias}, `resource_type` = #{resourceType}, `service_type` = #{serviceType}, `update_time` = #{updateTime} WHERE `guid` = #{guid}")
    void update( TaskElement taskElement );
    @Update("UPDATE `hydra_task_serv_node_meta` SET `name` = #{name} WHERE `guid` = #{guid}")
    void updateName( @Param("name") String name, @Param("guid") GUID guid );
    @Update("UPDATE `hydra_task_serv_node_meta` SET `path` = #{path} WHERE `guid` = #{guid}")
    void updatePath( @Param("path") String path, @Param("guid") GUID guid );
    @Update("UPDATE `hydra_task_serv_node_meta` SET `alias` = #{alias} WHERE `guid` = #{guid}")
    void updateAlias( @Param("alias") String alias, @Param("guid") GUID guid );
    @Update("UPDATE `hydra_task_serv_node_meta` SET `resource_type` = #{resourceType} WHERE `guid` = #{guid}")
    void updateResourceType( @Param("resourceType") String resourceType, @Param("guid") GUID guid );
    @Update("UPDATE `hydra_task_serv_node_meta` SET `service_type` = #{serviceType} WHERE `guid` = #{guid}")
    void updateTaskType( @Param("taskType") String serviceType, @Param("guid") GUID guid );
    @Update("UPDATE `hydra_task_serv_node_meta` SET `update_time` = #{updateTime} WHERE `guid` = #{guid}")
    void updateUpdateTime( @Param("updateTime") LocalDateTime updateTime, @Param("guid") GUID guid );

}
