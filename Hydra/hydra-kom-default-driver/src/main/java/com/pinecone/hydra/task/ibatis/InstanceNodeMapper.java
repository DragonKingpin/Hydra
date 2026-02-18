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

    String BASE_FIELDS =
            "guid, task_guid, name, task_name, business_time, priority, image_path, " +
                    "actually_priority, run_status, schedule_cycle_code, schedule_type_code, " +
                    "task_type, dry_run, run_count, sequence_cnt, retry_cnt, " +
                    "latest_start_time, latest_end_time, error_cause, create_time, update_time";

    String BASE_COLUMNS =
            "#{guid}, #{taskGuid}, #{instanceName}, #{taskName}, #{businessTime}, #{priority}, #{imagePath}, " +
                    "#{actuallyPriority}, #{runStatus}, #{kernelScheduleCycleCode}, #{kernelScheduleTypeCode}, " +
                    "#{taskType}, #{dryRun}, #{runCount}, #{sequenceCnt}, #{retryCnt}, " +
                    "#{lastStartTime}, #{lastEndTime}, #{errorCause}, #{createTime}, #{updateTime}";

    @Override
    @Insert(
            "INSERT INTO hydra_task_instances ( " + BASE_FIELDS + " ) VALUES ( " + BASE_COLUMNS + " )"
    )
    void insert( InstanceEntry instance );

    @Override
    @Update(
            "UPDATE hydra_task_instances SET " +
                    "task_guid = #{taskGuid}, " +
                    "name = #{instanceName}, " +
                    "task_name = #{taskName}," +
                    "business_time = #{businessTime}, " +
                    "priority = #{priority}, " +
                    "image_path = #{imagePath}, " +
                    "actually_priority = #{actuallyPriority}, " +
                    "run_status = #{runStatus}, " +
                    "schedule_cycle_code = #{kernelScheduleCycleCode}, " +
                    "schedule_type_code = #{kernelScheduleTypeCode}, " +
                    "task_type = #{taskType}, " +
                    "dry_run = #{dryRun}, " +
                    "run_count = #{runCount}, " +
                    "sequence_cnt = #{sequenceCnt}, " +
                    "retry_cnt = #{retryCnt}, " +
                    "latest_start_time = #{lastStartTime}, " +
                    "latest_end_time = #{lastEndTime}, " +
                    "error_cause = #{errorCause}, " +
                    "update_time = #{updateTime} " +
                    "WHERE guid = #{guid}"
    )
    void update( InstanceEntry instance );

    @Select(
            "SELECT " + BASE_FIELDS + " FROM hydra_task_instances WHERE guid = #{guid}"
    )
    @Results(id = "InstanceResultMap", value = {
            @Result(property = "guid", column = "guid"),
            @Result(property = "taskGuid", column = "task_guid"),
            @Result(property = "instanceName", column = "name"),
            @Result(property = "taskName", column = "task_name"),
            @Result(property = "businessTime", column = "business_time"),
            @Result(property = "priority", column = "priority"),
            @Result(property = "imagePath", column = "image_path"),
            @Result(property = "actuallyPriority", column = "actually_priority"),
            @Result(property = "runStatus", column = "run_status"),
            @Result(property = "kernelScheduleCycleCode", column = "schedule_cycle_code"),
            @Result(property = "kernelScheduleTypeCode", column = "schedule_type_code"),
            @Result(property = "taskType", column = "task_type"),
            @Result(property = "dryRun", column = "dry_run"),
            @Result(property = "runCount", column = "run_count"),
            @Result(property = "sequenceCnt", column = "sequence_cnt"),
            @Result(property = "retryCnt", column = "retry_cnt"),
            @Result(property = "lastStartTime", column = "latest_start_time"),
            @Result(property = "lastEndTime", column = "latest_end_time"),
            @Result(property = "errorCause", column = "error_cause"),
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
            "SELECT " + BASE_FIELDS +
                    " FROM hydra_task_instances LIMIT #{offset}, #{pageSize}"
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
            "SELECT " + BASE_FIELDS +
                    " FROM hydra_task_instances " +
                    "WHERE task_guid = #{taskGuid} " +
                    "LIMIT #{offset}, #{pageSize}"
    )
    @ResultMap("InstanceResultMap")
    List<GenericInstanceEntry> queryByTaskGuid0( @Param("taskGuid") GUID taskGuid, @Param("offset") long offset, @Param("pageSize") long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<InstanceEntry> queryByTaskGuid( TaskInstrument instrument, GUID taskGuid, long offset, long pageSize ) {
        List<GenericInstanceEntry> list = this.queryByTaskGuid0( taskGuid, offset, pageSize );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return (List) list;
    }

    @Select("SELECT COUNT(*) FROM `hydra_task_instances` WHERE task_guid = #{taskGuid}")
    long countInstanceByTaskGuid( GUID taskGuid );

    @Select(
            "SELECT " + BASE_FIELDS +
                    " FROM hydra_task_instances " +
                    "WHERE task_guid = #{taskGuid} and business_time = #{bizTime} " +
                    "ORDER BY run_count DESC LIMIT 1"
    )
    @ResultMap("InstanceResultMap")
    GenericInstanceEntry findLastExecuted0( @Param("taskGuid") GUID taskGuid, @Param("bizTime") String bizTime );

    @Override
    default InstanceEntry findLastExecuted( GUID taskGuid, TaskInstrument instrument, String bizTime ) {
        GenericInstanceEntry entry = this.findLastExecuted0( taskGuid, bizTime );
        if ( entry == null ) {
            return null;
        }
        entry.apply( instrument );
        return entry;
    }
}