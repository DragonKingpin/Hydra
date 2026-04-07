package com.walnut.odin.task.mapper;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.task.dto.CategoryTag;
import com.walnut.odin.task.dto.GenericCategoryTag;
import com.walnut.odin.task.source.CategoryMappingManipulator;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface CategoryMappingMapper extends CategoryMappingManipulator {

    @Override
    @Insert( "INSERT INTO `odin_task_category_mapping` ( `task_guid`, `category_type`, `category_name` ) " +
            "VALUES ( #{taskGuid}, #{categoryType}, #{categoryName} )" )
    void insert( CategoryTag categoryTag );

    @Select( "SELECT `id` AS enumId, `task_guid` AS taskGuid, `category_type` AS categoryType, `category_name` AS categoryName " +
            "FROM `odin_task_category_mapping` " +
            "WHERE `task_guid` = #{taskGuid}" )
    List<GenericCategoryTag> queryByTaskGuid0( @Param( "taskGuid" ) GUID taskGuid );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<CategoryTag> queryByTaskGuid( GUID taskGuid ) {
        return ( List ) this.queryByTaskGuid0( taskGuid );
    }


    @Override
    @Select( "SELECT `id` AS enumId, `task_guid` AS taskGuid, `category_type` AS categoryType, `category_name` AS categoryName " +
            "FROM `odin_task_category_mapping` " +
            "WHERE `task_guid` = #{taskGuid} " +
            "AND `category_type` = #{type} " +
            "AND `category_name` = #{name} " +
            "LIMIT 1" )
    GenericCategoryTag queryOwnedTag( @Param( "taskGuid" ) GUID taskGuid,
                                     @Param( "type" ) String type,
                                     @Param( "name" ) String name );

    @Override
    @Select( "SELECT COUNT( * ) " +
            "FROM `odin_task_category_mapping` " +
            "WHERE `category_type` = #{type} " +
            "AND `category_name` = #{name}" )
    long countTag( @Param( "type" ) String type, @Param( "name" ) String name );

    @Select( "SELECT `id` AS enumId, `task_guid` AS taskGuid, `category_type` AS categoryType, `category_name` AS categoryName " +
            "FROM `odin_task_category_mapping` " +
            "WHERE `category_type` = #{type} " +
            "AND `category_name` = #{name} " +
            "ORDER BY `id` ASC " +
            "LIMIT #{offset}, #{pageSize}" )
    List<GenericCategoryTag> queryTag0( @Param( "type" ) String type,
                                        @Param( "name" ) String name,
                                        @Param( "offset" ) long offset,
                                        @Param( "pageSize" ) long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<CategoryTag> queryTag( String type, String name, long offset, long pageSize ) {
        return ( List ) this.queryTag0( type, name, offset, pageSize );
    }

    @Override
    @Select( "SELECT COUNT( * ) " +
            "FROM `odin_task_category_mapping` " +
            "WHERE `category_name` = #{name}" )
    long countTagsByName( @Param( "name" ) String name );

    @Select( "SELECT `id` AS enumId, `task_guid` AS taskGuid, `category_type` AS categoryType, `category_name` AS categoryName " +
            "FROM `odin_task_category_mapping` " +
            "WHERE `category_name` = #{name} " +
            "ORDER BY `id` ASC " +
            "LIMIT #{offset}, #{pageSize}" )
    List<GenericCategoryTag> fetchByName0( @Param( "name" ) String name,
                                           @Param( "offset" ) long offset,
                                           @Param( "pageSize" ) long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<CategoryTag> fetchByName( String name, long offset, long pageSize ) {
        return ( List ) this.fetchByName0( name, offset, pageSize );
    }

    @Override
    @Update( "UPDATE `odin_task_category_mapping` " +
            "SET `task_guid` = #{taskGuid}, " +
            "`category_type` = #{categoryType}, " +
            "`category_name` = #{categoryName} " +
            "WHERE `id` = #{enumId}" )
    void update( CategoryTag categoryTag );

    @Delete( "<script>" +
            "DELETE FROM `odin_task_category_mapping` " +
            "<where> " +
            "    1 = 1" +
            "    <if test='taskGuid != null'>AND `task_guid` = #{taskGuid} </if> " +
            "    <if test='type != null'>AND `category_type` = #{type} </if> " +
            "    <if test='name != null'>AND `category_name` = #{name} </if> " +
            "    <if test='taskGuid == null and type == null and name == null'>\n" +
            "       AND 1 = 0\n" + // Prevent to eradicate the whole table.
            "    </if>" +
            "</where>" +
            "</script>" )
    void purge( @Param( "taskGuid" ) GUID taskGuid,
                @Param( "type" ) String type,
                @Param( "name" ) String name );

}
