package com.walnut.odin.task.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.dispatch.entity.GenericTaskProcessorEntity;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.task.source.TaskProcessorManipulator;

@Mapper
@IbatisDataAccessObject
public interface TaskProcessorMapper extends TaskProcessorManipulator {

    @Select(
        "SELECT " +
        "  `id`, " +
        "  `guid`, " +
        "  `processor_name` AS name, " +
        "  `cluster_path` AS clusterPath, " +
        "  `cluster_name` AS clusterName, " +
        "  `is_local` AS `local`, " +
        "  `is_exclusive` AS exclusive, " +
        "  `priority`, " +
        "  `queue_name` AS queueName, " +
        "  `queue_max_capacity` AS queueMaxCapacity, " +
        "  `queue_min_capacity` AS queueMinCapacity, " +
        "  `queue_runtime_instance_capacity` AS queueRuntimeInstanceCapacity, " +
        "  `enable`, " +
        "  `create_time` AS createTime, " +
        "  `update_time` AS updateTime " +
        "FROM `odin_task_processor` " +
        "WHERE `processor_name` = #{name}"
    )
    GenericTaskProcessorEntity selectByProcessorName( @Param("name") String szProcessorName );


    @Select(
        "SELECT " +
        "  `id`, " +
        "  `guid`, " +
        "  `processor_name` AS name, " +
        "  `cluster_path` AS clusterPath, " +
        "  `cluster_name` AS clusterName, " +
        "  `is_local` AS `local`, " +
        "  `is_exclusive` AS exclusive, " +
        "  `priority`, " +
        "  `queue_name` AS queueName, " +
        "  `queue_max_capacity` AS queueMaxCapacity, " +
        "  `queue_min_capacity` AS queueMinCapacity, " +
        "  `queue_runtime_instance_capacity` AS queueRuntimeInstanceCapacity, " +
        "  `enable`, " +
        "  `create_time` AS createTime, " +
        "  `update_time` AS updateTime " +
        "FROM `odin_task_processor` " +
        "WHERE `guid` = #{guid} AND `enable` = 1"
    )
    GenericTaskProcessorEntity selectByGuid( @Param("guid") GUID guid );


    @Select(
        "SELECT " +
        "  `id`, " +
        "  `guid`, " +
        "  `processor_name` AS name, " +
        "  `cluster_path` AS clusterPath, " +
        "  `cluster_name` AS clusterName, " +
        "  `is_local` AS `local`, " +
        "  `is_exclusive` AS exclusive, " +
        "  `priority`, " +
        "  `queue_name` AS queueName, " +
        "  `queue_max_capacity` AS queueMaxCapacity, " +
        "  `queue_min_capacity` AS queueMinCapacity, " +
        "  `queue_runtime_instance_capacity` AS queueRuntimeInstanceCapacity, " +
        "  `enable`, " +
        "  `create_time` AS createTime, " +
        "  `update_time` AS updateTime " +
        "FROM `odin_task_processor` " +
        "WHERE `cluster_name` = #{clusterName} AND `enable` = 1"
    )
    List<GenericTaskProcessorEntity> selectByClusterName0( @Param("clusterName") String clusterName );


    @Override
    @SuppressWarnings("unchecked")
    default List<TaskProcessorEntity> selectByClusterName( @Param("clusterName") String clusterName ) {
        return (List) this.selectByClusterName0( clusterName );
    }


    @Select(
        "SELECT " +
        "  `id`, " +
        "  `guid`, " +
        "  `processor_name` AS name, " +
        "  `cluster_path` AS clusterPath, " +
        "  `cluster_name` AS clusterName, " +
        "  `is_local` AS `local`, " +
        "  `is_exclusive` AS exclusive, " +
        "  `priority`, " +
        "  `queue_name` AS queueName, " +
        "  `queue_max_capacity` AS queueMaxCapacity, " +
        "  `queue_min_capacity` AS queueMinCapacity, " +
        "  `queue_runtime_instance_capacity` AS queueRuntimeInstanceCapacity, " +
        "  `enable`, " +
        "  `create_time` AS createTime, " +
        "  `update_time` AS updateTime " +
        "FROM `odin_task_processor`"
    )
    List<GenericTaskProcessorEntity> selectAll0();


    @Override
    @SuppressWarnings("unchecked")
    default List<TaskProcessorEntity> selectAll() {
        return (List) this.selectAll0();
    }


    @Insert(
        "INSERT INTO `odin_task_processor` ( " +
        "  `guid`, " +
        "  `processor_name`, " +
        "  `cluster_path`, " +
        "  `cluster_name`, " +
        "  `is_local`, " +
        "  `is_exclusive`, " +
        "  `priority`, " +
        "  `queue_name`, " +
        "  `queue_max_capacity`, " +
        "  `queue_min_capacity`, " +
        "  `queue_runtime_instance_capacity` " +
        ") VALUES ( " +
        "  #{entity.guid}, " +
        "  #{entity.name}, " +
        "  #{entity.clusterPath}, " +
        "  #{entity.clusterName}, " +
        "  #{entity.isLocal}, " +
        "  #{entity.isExclusive}, " +
        "  #{entity.priority}, " +
        "  #{entity.queueName}, " +
        "  #{entity.queueMaxCapacity}, " +
        "  #{entity.queueMinCapacity}, " +
        "  #{entity.queueRuntimeInstanceCapacity} " +
        ")"
    )
    int insert( @Param("entity") TaskProcessorEntity entity );


    @Update(
        "UPDATE `odin_task_processor` SET " +
        "  `cluster_path` = #{entity.clusterPath}, " +
        "  `cluster_name` = #{entity.clusterName}, " +
        "  `is_local` = #{entity.isLocal}, " +
        "  `is_exclusive` = #{entity.isExclusive}, " +
        "  `priority` = #{entity.priority}, " +
        "  `queue_name` = #{entity.queueName}, " +
        "  `queue_max_capacity` = #{entity.queueMaxCapacity}, " +
        "  `queue_min_capacity` = #{entity.queueMinCapacity}, " +
        "  `queue_runtime_instance_capacity` = #{entity.queueRuntimeInstanceCapacity}, " +
        "  `enable` = #{entity.enable} " +
        "WHERE `guid` = #{entity.guid}"
    )
    int updateByGuid( @Param("entity") GenericTaskProcessorEntity entity );


    @Update(
        "UPDATE `odin_task_processor` SET " +
        "  `queue_max_capacity` = #{maxCapacity}, " +
        "  `queue_min_capacity` = #{minCapacity}, " +
        "  `queue_runtime_instance_capacity` = #{runtimeCapacity} " +
        "WHERE `guid` = #{guid}"
    )
    int updateQueueCapacity(
            @Param("guid") GUID guid,
            @Param("maxCapacity") int maxCapacity,
            @Param("minCapacity") int minCapacity,
            @Param("runtimeCapacity") int runtimeCapacity
    );


    @Update(
        "DELETE FROM `odin_task_processor` WHERE `guid` = #{guid}"
    )
    int deleteByGuid( @Param("guid") GUID guid );


    @Update(
        "UPDATE `odin_task_processor` SET `enable` = 1 WHERE `guid` = #{guid}"
    )
    int enable( @Param("guid") GUID guid );


    @Update(
        "UPDATE `odin_task_processor` SET `enable` = 0 WHERE `guid` = #{guid}"
    )
    int disable( @Param("guid") GUID guid );

}