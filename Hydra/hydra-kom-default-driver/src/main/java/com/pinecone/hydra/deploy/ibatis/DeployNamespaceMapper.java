package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.entity.GenericNamespace;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.source.DeployNamespaceManipulator;


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
public interface DeployNamespaceMapper extends DeployNamespaceManipulator {

    @Override
    @Insert("INSERT INTO `hydra_deploy_namespace_node` (`guid`, `name`) VALUES (#{guid},#{name})")
    void insert( Namespace ns );

    @Override
    @Delete("DELETE FROM `hydra_deploy_namespace_node` WHERE `guid`=#{guid}")
    void remove( @Param("guid") GUID GUID );

    @Override
    @Select("SELECT `id` AS `enumId`, `guid`, `name` FROM `hydra_deploy_namespace_node` WHERE `guid`=#{guid}")
    GenericNamespace getNamespace( @Param("guid") GUID guid );

    @Override
    @Update("UPDATE `hydra_deploy_namespace_node` SET `name` = #{name} WHERE `guid` = #{guid}")
    void update( Namespace ns );

    @Select("SELECT `id` AS `enumId`, `guid`, `name` FROM `hydra_deploy_namespace_node` WHERE name=#{name}")
    List<GenericNamespace > fetchNamespaceNodeByName0( @Param("name") String name );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<Namespace > fetchNamespaceNodeByName( String name ){
        return (List) this.fetchNamespaceNodeByName0( name );
    }

    @Override
    @Select( "SELECT `guid` FROM `hydra_deploy_namespace_node` WHERE `name` = #{name}" )
    List<GUID > getGuidsByName(String name);

    @Override
    @Select( "SELECT `guid` FROM `hydra_deploy_namespace_node` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );

}
