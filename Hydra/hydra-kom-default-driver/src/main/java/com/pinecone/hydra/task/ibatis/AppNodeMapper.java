package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.task.kom.entity.GenericAppElement;
import com.pinecone.hydra.task.kom.source.AppNodeManipulator;
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
public interface AppNodeMapper extends AppNodeManipulator {

    @Override
    @Insert("INSERT INTO `hydra_task_app_node` " +
            "(`guid`, `name`, `type`, `create_time`, `update_time`) " +
            "VALUES (#{guid}, #{name}, #{type}, #{createTime}, #{updateTime})")
    void insert( AppElement appElement);

    @Override
    @Delete("DELETE FROM `hydra_task_app_node` WHERE `guid` = #{guid}")
    void remove(@Param("guid") GUID guid);

    @Select("SELECT `id` AS `enumId`, `guid`, `name`, `type`, " +
            "`create_time` AS `createTime`, `update_time` AS `updateTime` " +
            "FROM `hydra_task_app_node` WHERE `guid` = #{guid}")
    GenericAppElement getAppElement(@Param("guid") GUID guid);

    @Override
    default AppElement getAppElement(GUID guid, TaskInstrument instrument ) {
        GenericAppElement element = this.getAppElement( guid );
        element.apply( instrument );

        return element;
    }

    @Override
    @Update("UPDATE `hydra_task_app_node` SET " +
            "`name` = #{name}, " +
            "`type` = #{type}, " +
            "`create_time` = #{createTime}, " +
            "`update_time` = #{updateTime} " +
            "WHERE `guid` = #{guid}")
    void update( AppElement appElement);


    @Override
    @Select( "SELECT `guid` FROM `hydra_task_app_node` WHERE `name` = #{name}" )
    List<GUID > getGuidsByName( String name );

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_app_node` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
