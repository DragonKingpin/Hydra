package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.deploy.kom.source.DeployNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface DeployNodeMapper extends DeployNodeManipulator {
    @Override
    @Insert( "INSERT INTO `hydra_deploy_deploy_nodes` (`guid`,`enable`,`name`) VALUES (#{guid}, #{enable}, #{name})")
    void insert( DeployElement deployElement );

    @Override
    @Delete("DELETE FROM `hydra_deploy_deploy_nodes` WHERE `guid`=#{guid}")
    void remove( GUID UUID );
    @Override
    @Insert( "UPDATE `hydra_deploy_deploy_nodes` SET `enable`=#{enable}, `name`=#{name} WHERE `guid`=#{guid}")
    void update( DeployElement deployElement );
    @Override
    @Select("SELECT `guid`, `enable` AS Enable FROM `hydra_deploy_deploy_nodes` WHERE `name`=#{name}")
    List<DeployElement> fetchDeployNodeByName( @Param("name") String name );

    @Select("SELECT `guid` FROM `hydra_deploy_deploy_nodes` WHERE `name`=#{name}")
    @Override
    List<GUID> getGuidsByName( String name );

    @Select("SELECT `guid` FROM `hydra_deploy_deploy_nodes` WHERE `name`=#{name} AND `guid`!=#{guid}")
    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );

}
