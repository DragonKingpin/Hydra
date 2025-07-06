package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface TaskNodeMapper extends TaskNodeManipulator {

    @Override
    @Insert("INSERT INTO `hydra_task_task_node` " +
            "(`guid`, `name`, `image_path`, `type`, `resource_type`, `deployment_method`, `priority`, `actually_priority`, " +
            "`dry_run`, `schedule_cycle_code`, `schedule_type_code`, `enable`, " +
            "`create_time`, `update_time`) " +
            "VALUES (#{guid}, #{name}, #{imagePath}, #{type}, #{resourceType}, #{deploymentMethod}, #{priority}, #{actuallyPriority}, " +
            "#{dryRun}, #{scheduleCycleCode}, #{scheduleTypeCode}, #{enable}, " +
            "#{createTime}, #{updateTime})")
    void insert( TaskElement taskElement );

    @Override
    @Delete("DELETE FROM `hydra_task_task_node` WHERE `guid`=#{guid}")
    void remove( @Param("guid") GUID guid );

    @Select("SELECT `id` AS `enumId`, `guid`, `name`, `image_path` AS `imagePath`, `type`, " +
            "`resource_type` AS `resourceType`, `deployment_method` AS `deploymentMethod`, `priority`, `actually_priority` as actuallyPriority, " +
            "`dry_run` AS `dryRun`, `schedule_cycle_code` AS `scheduleCycleCode`, `schedule_type_code` AS `scheduleTypeCode`, `enable` AS `enable`, " +
            "`create_time` AS `createTime`, `update_time` AS `updateTime` " +
            "FROM `hydra_task_task_node` WHERE `guid` = #{guid}")
    GenericTaskElement getTaskNode0( @Param("guid") GUID guid );

    @Override
    default TaskElement getTaskNode( GUID guid, TaskInstrument instrument ) {
        GenericTaskElement taskElement = this.getTaskNode0( guid );
        taskElement.apply( instrument );
        return taskElement;
    }

    @Override
    @Update("UPDATE `hydra_task_task_node` SET " +
            "`name` = #{name}, " +
            "`image_path` = #{imagePath}, " +
            "`type` = #{type}, " +
            "`resource_type` = #{resourceType}, " +
            "`deployment_method` = #{deploymentMethod}, " +
            "`priority` = #{priority}, " +
            "`actually_priority` = #{actuallyPriority}, " +
            "`dry_run` = #{dryRun}, " +
            "`schedule_cycle_code` = #{scheduleCycleCode}, " +
            "`schedule_type_code` = #{scheduleTypeCode}, " +
            "`enable` = #{enable}, " +
            "`create_time` = #{createTime}, " +
            "`update_time` = #{updateTime} " +
            "WHERE `guid` = #{guid}")
    void update( TaskElement taskElement );

    @Select("SELECT `id` AS `enumId`, `guid`, `name`, `image_path` AS `imagePath`, `type`, " +
            "`resource_type` AS `resourceType`, `deployment_method` AS `deploymentMethod`, `priority`, `actually_priority` as actuallyPriority, " +
            "`dry_run` AS `dryRun`, `schedule_cycle_code` AS `scheduleCycleCode`, `schedule_type_code` AS `scheduleTypeCode`, `enable` AS `enable`, " +
            "`create_time` AS `createTime`, `update_time` AS `updateTime` " +
            "FROM `hydra_task_task_node` WHERE `name` = #{name}")
    List<GenericTaskElement> fetchTaskNodeByName0( @Param("name") String name );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskElement> fetchTaskNodeByName( String name ) {
        List<GenericTaskElement> list = this.fetchTaskNodeByName0( name );
        return (List) list;
    }

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_task_node` WHERE `name` = #{name}" )
    List<GUID> getGuidsByName( String name );

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_task_node` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );


}
