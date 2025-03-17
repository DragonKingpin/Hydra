package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;
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

import java.util.ArrayList;
import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface TaskNodeMapper extends TaskNodeManipulator {
    @Insert("INSERT INTO `hydra_task_task_nodes` (`guid`, `name`) VALUES (#{guid},#{name})")
    void insert(GenericTaskElement taskNode);

    @Delete("DELETE FROM `hydra_task_task_nodes` WHERE `guid`=#{guid}")
    void remove(@Param("guid")GUID guid);

    @Select("SELECT `id` AS `enumId`, `guid`, `name` FROM `hydra_task_task_nodes` WHERE `guid`=#{guid}")
    GenericTaskElement getTaskNode(@Param("guid") GUID guid);

    @Update("UPDATE `hydra_task_task_nodes` SET `name` = #{name} WHERE `guid` = #{guid}")
    void update(GenericTaskElement serviceNode);

    @Select("SELECT `id` AS `enumId`, `guid` , `name` FROM `hydra_task_task_nodes` WHERE name=#{name}")
    List<GenericTaskElement> fetchTaskNodeByName(@Param("name") String name);

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_task_nodes` WHERE `name` = #{name}" )
    List<GUID> getGuidsByName( String name );

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_task_nodes` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );


    default List<TaskElement> fetchAllTask(){
        List<TaskElement> taskElements = this.fetchAllTask();
        return new ArrayList<>(taskElements);
    }

    @Select("SELECT `id`, `guid`, `name` FROM `hydra_task_task_nodes` ")
    List<GenericTaskElement> fetchAllTask0();
}
