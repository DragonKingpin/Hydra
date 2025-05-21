package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.GenericInstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
@IbatisDataAccessObject
public interface InstanceNodeMapper extends InstanceNodeManipulator {

    @Override
    @Insert(
            "INSERT INTO hydra_task_instances (" +
                    "guid, task_guid, name, business_time, priority, " +
                    "actually_priority, run_status, schedule_cycle_code, schedule_type_code, " +
                    "task_type, dry_run, run_count, latest_start_time, latest_end_time, " +
                    "create_time, update_time" +
                    ") VALUES (" +
                    "#{guid}, #{affiliatedTaskGuid}, #{instanceName}, #{businessTime}, #{priority}, " +
                    "#{actuallyPriority}, #{instanceStatus}, #{kernelScheduleCycle}, #{kernelScheduleType}, " +
                    "#{taskType}, #{dryRun}, #{runCount}, #{lastStartTime}, #{lastEndTime}, " +
                    "#{createTime}, #{updateTime}" +
                    ")"
    )
    void insert( InstanceEntry instance );

    @Override
    @Update(
            "UPDATE hydra_task_instances SET " +
                    "task_guid = #{affiliatedTaskGuid}, " +
                    "name = #{instanceName}, " +
                    "business_time = #{businessTime}, " +
                    "priority = #{priority}, " +
                    "actually_priority = #{actuallyPriority}, " +
                    "run_status = #{instanceStatus}, " +
                    "schedule_cycle_code = #{kernelScheduleCycle}, " +
                    "schedule_type_code = #{kernelScheduleType}, " +
                    "task_type = #{taskType}, " +
                    "dry_run = #{dryRun}, " +
                    "run_count = #{runCount}, " +
                    "latest_start_time = #{lastStartTime}, " +
                    "latest_end_time = #{lastEndTime}, " +
                    "update_time = #{updateTime} " +
                    "WHERE guid = #{guid}"
    )
    void update( InstanceEntry instance );

    @Select(
            "SELECT " +
                    "guid, task_guid, name, business_time, priority, " +
                    "actually_priority, run_status, schedule_cycle_code, schedule_type_code, " +
                    "task_type, dry_run, run_count, latest_start_time, latest_end_time, " +
                    "create_time, update_time " +
                    "FROM hydra_task_instances WHERE guid = #{guid}"
    )
    @Results(id = "InstanceResultMap", value = {
            @Result(property = "guid", column = "guid"),
            @Result(property = "affiliatedTaskGuid", column = "task_guid"),
            @Result(property = "instanceName", column = "name"),
            @Result(property = "businessTime", column = "business_time"),
            @Result(property = "priority", column = "priority"),
            @Result(property = "actuallyPriority", column = "actually_priority"),
            @Result(property = "instanceStatus", column = "run_status"),
            @Result(property = "kernelScheduleCycle", column = "schedule_cycle_code"),
            @Result(property = "kernelScheduleType", column = "schedule_type_code"),
            @Result(property = "taskType", column = "task_type"),
            @Result(property = "dryRun", column = "dry_run"),
            @Result(property = "runCount", column = "run_count"),
            @Result(property = "lastStartTime", column = "latest_start_time"),
            @Result(property = "lastEndTime", column = "latest_end_time"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    GenericInstanceEntry queryByGuid0( GUID guid );

    @Override
    default InstanceEntry queryByGuid( GUID guid, TaskInstrument instrument ) {
        GenericInstanceEntry entry = this.queryByGuid0( guid );
        if ( entry == null ) {
            return null;
        }
        entry.apply( instrument );
        return entry;
    }

    @Override
    @Select("SELECT COUNT(*) FROM `hydra_task_instances`")
    int countInstance();

    @Select("SELECT COUNT(*) FROM `hydra_task_instances` WHERE name = #{name}")
    long countInstanceByName( String name );

    @Select(
            "SELECT " +
                    "guid, task_guid, name, business_time, priority, " +
                    "actually_priority, run_status, schedule_cycle_code, schedule_type_code, " +
                    "task_type, dry_run, run_count, latest_start_time, latest_end_time, " +
                    "create_time, update_time " +
                    "FROM hydra_task_instances LIMIT #{offset}, #{pageSize}"
    )
    @ResultMap("InstanceResultMap")
    List<GenericInstanceEntry> fetchInstances0( @Param("offset") long offset, @Param("pageSize") long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<InstanceEntry> fetchInstances( TaskInstrument instrument, long offset, long pageSize ) {
        List<GenericInstanceEntry> list = this.fetchInstances0( offset, pageSize );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return (List) list;
    }

    @Select(
            "SELECT " +
                    "guid, task_guid, name, business_time, priority, " +
                    "actually_priority, run_status, schedule_cycle_code, schedule_type_code, " +
                    "task_type, dry_run, run_count, latest_start_time, latest_end_time, " +
                    "create_time, update_time " +
                    "FROM hydra_task_instances " +
                    "WHERE task_guid = #{taskGuid}" +
                    "LIMIT #{offset}, #{pageSize}"
    )
    @ResultMap("InstanceResultMap")
    List<GenericInstanceEntry> queryByTaskGuid0( @Param("taskGuid") GUID taskGuid, @Param("offset") long offset, @Param("pageSize") long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<InstanceEntry> queryByTaskGuid( TaskInstrument instrument, GUID taskGuid, long offset, long pageSize ) {
        List<GenericInstanceEntry> list = this.queryByTaskGuid0( taskGuid, offset, pageSize );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply(instrument);
        }
        return (List) list;
    }

    @Select("SELECT COUNT(*) FROM `hydra_task_instances` WHERE task_guid = #{taskGuid}")
    long countInstanceByTaskGuid( GUID taskGuid );

}
