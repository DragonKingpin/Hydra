package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.kom.source.ServiceInstanceManipulator;
import com.pinecone.hydra.service.registry.dao.ServiceInstanceDO;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface ServiceInstanceMapper extends ServiceInstanceManipulator {
    @Override
    @Insert("INSERT INTO `hydra_service_service_instance` " +
            "(`service_guid`, `guid`, `status`, `latest_start_time`, `latest_end_time`, `error_cause`, `run_count`, `deploy_guid`, `ip`) VALUES " +
            "(#{serviceGuid}, #{guid}, #{status}, #{latestStartTime}, #{latestEndTime}, #{errorCause}, #{runCount}, #{deployGuid}, #{ip})")
    void initServiceInstance(ServiceInstanceEntry element);

    @Override
    @Select("SELECT `id`, `service_guid`, `guid`, `status`, `latest_start_time`, `latest_end_time`, `error_cause`, `run_count`, `deploy_guid`, `ip`" +
            " FROM `hydra_service_service_instance` WHERE guid = #{serviceId}")
    ServiceInstanceDO queryServiceInstance(GUID serviceId);

    @Override
    @Update("UPDATE `hydra_service_service_instance` SET status = #{status}, run_count = #{runCount} WHERE guid = #{guid}")
    void updateServiceInstance(ServiceInstanceEntry element);
}
