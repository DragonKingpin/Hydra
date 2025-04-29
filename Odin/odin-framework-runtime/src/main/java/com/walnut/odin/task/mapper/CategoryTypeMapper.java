package com.walnut.odin.task.mapper;

import java.util.List;

import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.task.entity.pyramid.GenericCategoryType;
import com.walnut.odin.task.entity.pyramid.CategoryType;
import com.walnut.odin.task.source.CategoryTypeManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface CategoryTypeMapper extends CategoryTypeManipulator {

    @Override
    @Insert("INSERT INTO `odin_task_category_type` (`name`, `alias`, `description`) " +
            "VALUES (#{name}, #{alias}, #{description})")
    void insert( CategoryType categoryType );

    @Override
    @Select("SELECT `id` as enumId, `name`, `alias`, `description` " +
            "FROM `odin_task_category_type` " +
            "WHERE `name` = #{name}")
    GenericCategoryType queryType( String name );

    @Override
    @Select( "SELECT COUNT(*) FROM `odin_task_category_type`" )
    long countTypes( );

    @Select( "SELECT `id` AS enumId, `name`, `alias`, `description` " +
            "FROM `odin_task_category_type` " +
            "ORDER BY `id` ASC " +
            "LIMIT #{offset}, #{pageSize}" )
    List<GenericCategoryType> fetchType0( @Param( "offset" ) long offset, @Param( "pageSize" ) long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<CategoryType> fetchType( long offset, long pageSize ) {
        return (List) this.fetchType0( offset, pageSize );
    }

    @Override
    @Delete("DELETE FROM `odin_task_category_type` " +
            "WHERE `name` = #{name}")
    void remove( String name );

    @Override
    @Update("UPDATE `odin_task_category_type` " +
            "SET `alias` = #{alias}, `description` = #{description} " +
            "WHERE `name` = #{name}")
    void update( CategoryType categoryType);

}