package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.ServiceInstrument;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.task.kom.entity.GenericJobElement;
import com.pinecone.hydra.task.kom.source.JobMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface JobNodeMetaMapper extends JobMetaManipulator {
    @Insert( "INSERT INTO `hydra_task_job_node_meta` (`guid`, `name`, `path`, `type`, `alias`, resource_type, deployment_method, create_time, update_time) VALUES (#{metaGuid},#{name},#{path},#{type},#{alias},#{resourceType},#{deploymentMethod},#{createTime},#{updateTime})" )
    void insert( JobElement jobElement );

    @Delete( "DELETE FROM `hydra_task_job_node_meta` WHERE `guid`=#{guid}" )
    void remove( @Param("guid") GUID guid );

    @Select( "SELECT `id` AS `enumId`, `guid`, `name`, `path`, `type`, `alias`, `resource_type` AS resourceType, `deployment_method` AS deploymentMethod, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_task_job_node_meta` WHERE `guid`=#{guid}" )
    GenericJobElement getJobElement( @Param("guid") GUID guid );

    @Override
    default GenericJobElement getJobElement( GUID guid, ServiceInstrument serviceInstrument){
        GenericJobElement element = this.getJobElement( guid );
        element.apply(serviceInstrument);
        return element;
    }

    @Update("UPDATE `hydra_task_job_node_meta` SET `name` = #{name}, `path` = #{path}, `type` = #{type}, `alias` = #{alias}, `resource_type` = #{resourceType}, `deployment_method` = #{deploymentMethod}, `update_time` = #{updateTime} WHERE `guid` = #{guid}")
    void update( JobElement jobElement );

}
