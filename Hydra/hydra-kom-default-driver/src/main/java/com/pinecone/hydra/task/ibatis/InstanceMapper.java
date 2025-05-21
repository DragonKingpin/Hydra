package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.dto.GenericInstance;
import com.pinecone.hydra.task.kom.instance.source.InstanceMappingManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
@IbatisDataAccessObject
public interface InstanceMapper extends InstanceMappingManipulator {

    @Insert("INSERT INTO `hydra_task_instance` (guid, name, business_date, priority, run_status, schedule_cycle, task_type, generation_mode, run_count, schedule_type, latest_start_time, latest_end_time) VALUES (#{guid}, #{name}, #{businessDate}, #{priority}, #{runStatus}, #{scheduleCycle}, #{taskType}, #{generationMode}, #{runCount}, #{scheduleType}, #{lastStartTime}, #{lastEndTime})")
    void insert( GenericInstance instance );

    @Insert("UPDATE `hydra_task_instance` SET business_date = #{businessDate}, priority = #{priority}, run_status = #{runStatus}, schedule_cycle = #{scheduleCycle}, task_type = #{taskType}, generation_mode = #{generationMode}, run_count = #{runCount}, schedule_type = #{scheduleType}, latest_start_time = #{lastStartTime}, latest_end_time = #{lastEndTime} WHERE guid = #{guid}")
    void update( GenericInstance instance );

    @Select("SELECT  business_date AS businessDate, priority, run_status AS runStatus, schedule_cycle AS scheduleCycle, task_type AS taskType, generation_mode AS generationMode, run_count,run_count AS runCount, schedule_type AS scheduleType, latest_start_time AS latestStartTime, latest_end_time AS latestEndTime, name FROM `hydra_task_instance` WHERE guid = #{guid}")
    GenericInstance queryByGuid( GUID guid );

    @Select("SELECT  business_date AS businessDate, priority, run_status AS runStatus, schedule_cycle AS scheduleCycle, task_type AS taskType, generation_mode AS generationMode, run_count,run_count AS runCount, schedule_type AS scheduleType, latest_start_time AS latestStartTime, latest_end_time AS latestEndTime, name FROM `hydra_task_instance` WHERE name = #{name}")
    GenericInstance queryByName( String name );

    @Select("SELECT COUNT(*) FROM `hydra_task_instance`")
    int countInstance();

    @Select("SELECT COUNT(*) FROM `hydra_task_instance` WHERE name = #{name}")
    long countInstanceByName( String name );

    @Select("SELECT  business_date AS businessDate, priority, run_status AS runStatus, schedule_cycle AS scheduleCycle, task_type AS taskType, generation_mode AS generationMode, run_count,run_count AS runCount, schedule_type AS scheduleType, latest_start_time AS latestStartTime, latest_end_time AS latestEndTime, name FROM `hydra_task_instance` WHERE guid = #{guid} ORDER BY latest_start_time DESC LIMIT #{pageSize} , #{offset} ")
    List<GenericInstance> fetchInstanceByGuid(long offset, long pageSize );

    @Select("SELECT  business_date AS businessDate, priority, run_status AS runStatus, schedule_cycle AS scheduleCycle, task_type AS taskType, generation_mode AS generationMode, run_count,run_count AS runCount, schedule_type AS scheduleType, latest_start_time AS latestStartTime, latest_end_time AS latestEndTime, name FROM `hydra_task_instance` WHERE task_guid = #{taskGuid}")
    List<GenericInstance> queryByTaskGuid(GUID guid );

    @Select("SELECT COUNT(*) FROM `hydra_task_instance` WHERE task_guid = #{taskGuid}")
    long countInstanceByTaskGuid(GUID taskGuid);

    @Override
    @SuppressWarnings( "unchecked" )
    default List<GenericInstance> fetchInstances(long offset, long pageSize) {
        return (List) this.fetchInstanceByGuid( offset, pageSize );
    }

}
