package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.source.TaskNamespaceManipulator;
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
public interface TaskNamespaceMapper extends TaskNamespaceManipulator {
    @Insert("INSERT INTO `hydra_task_namespace_node` (`guid`, `name`) VALUES (#{guid},#{name})")
    void insert( Namespace ns );

    @Delete("DELETE FROM `hydra_task_namespace_node` WHERE `guid`=#{guid}")
    void remove( @Param("guid") GUID GUID );

    @Select("SELECT `id` AS `enumId`, `guid`, `name` FROM `hydra_task_namespace_node` WHERE `guid`=#{guid}")
    GenericNamespace getNamespace( @Param("guid") GUID guid );

    @Update("UPDATE `hydra_task_namespace_node` SET `name` = #{name} WHERE `guid` = #{guid}")
    void update( Namespace ns );

    @Select("SELECT `id` AS `enumId`, `guid`, `name` FROM `hydra_task_namespace_node` WHERE name=#{name}")
    List<GenericNamespace > fetchNamespaceNodeByName0( @Param("name") String name );

    @SuppressWarnings( "unchecked" )
    default List<Namespace > fetchNamespaceNodeByName( String name ){
        return (List) this.fetchNamespaceNodeByName0( name );
    }

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_namespace_node` WHERE `name` = #{name}" )
    List<GUID > getGuidsByName(String name);

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_namespace_node` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
