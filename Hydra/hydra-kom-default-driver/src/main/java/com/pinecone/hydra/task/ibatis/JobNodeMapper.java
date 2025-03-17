package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.task.kom.entity.GenericJobElement;
import com.pinecone.hydra.task.kom.source.JobNodeManipulator;
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
public interface JobNodeMapper extends JobNodeManipulator {
    @Insert("INSERT INTO  `hydra_task_job_node` (`guid`, `name`) VALUES (#{guid},#{name})")
    void insert( JobElement jobElement );

    @Delete("DELETE FROM `hydra_task_job_node` WHERE `guid`=#{guid}")
    void remove( @Param("guid")GUID guid );

    @Select("SELECT `id` AS `enumId`, `guid`, `name` FROM `hydra_task_job_node` WHERE `guid`=#{guid}")
    GenericJobElement getJobElement(@Param("guid")GUID guid);

    @Update("UPDATE `hydra_task_job_node` SET name = #{name} WHERE guid = #{guid}")
    void update( JobElement jobElement );

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_job_node` WHERE `name` = #{name}" )
    List<GUID > getGuidsByName( String name );

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_job_node` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
