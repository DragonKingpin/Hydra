package com.walnut.odin.task.mapper;

import java.util.List;

import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.task.entity.GenericTaskCategory;
import com.walnut.odin.task.entity.TaskCategory;
import com.walnut.odin.task.source.TaskCategoryManipulator;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface TaskCategoryMapper extends TaskCategoryManipulator {

    @Override
    @Insert( "INSERT INTO `odin_task_category` ( `name`, `alias`, `description` ) " +
            "VALUES ( #{name}, #{alias}, #{description} )" )
    void insert( TaskCategory taskCategory );

    @Override
    @Select( "SELECT `id` AS enumId, `name`, `alias`, `description` " +
            "FROM `odin_task_category` " +
            "WHERE `name` = #{name}" )
    GenericTaskCategory queryTaskCategory( String name );

    @Override
    @Select( "SELECT COUNT(*) FROM `odin_task_category`" )
    long countCategories( );

    @Select( "SELECT `id` AS enumId, `name`, `alias`, `description` " +
            "FROM `odin_task_category` " +
            "ORDER BY `id` ASC " +
            "LIMIT #{offset}, #{pageSize}" )
    List<GenericTaskCategory> fetchCategory0( @Param( "offset" ) long offset, @Param( "pageSize" ) long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskCategory> fetchCategory( long offset, long pageSize ) {
        return (List) this.fetchCategory0( offset, pageSize );
    }

    @Override
    @Delete( "DELETE FROM `odin_task_category` " +
            "WHERE `name` = #{name}" )
    void remove( String name );

    @Override
    @Update( "UPDATE `odin_task_category` " +
            "SET `alias` = #{alias}, `description` = #{description} " +
            "WHERE `name` = #{name}" )
    void update( TaskCategory taskCategory );

}
