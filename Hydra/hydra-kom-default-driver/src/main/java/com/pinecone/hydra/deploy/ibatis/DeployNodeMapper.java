package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
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

@Insert( "INSERT INTO `hydra_deploy_deploy_nodes` (`guid`,`enable`,`name`) VALUES (#{guid}, #{enable}, #{name})")
    void insert( DeployElement deployElement );

@Delete("DELETE FROM `hydra_deploy_deploy_nodes` WHERE `guid`=#{guid}")
    void remove( GUID UUID );

@Select("SELECT `guid`, `enable` FROM `hydra_deploy_deploy_nodes` WHERE `guid`=#{guid}")
    DeployElement getDeployNode( GUID guid, DeployInstrument instrument );

    void update( DeployElement deployElement );

@Select("SELECT `guid`, `enable` AS Enable FROM `hydra_deploy_deploy_nodes` WHERE `name`=#{name}")
    List<DeployElement> fetchDeployNodeByName( @Param("name") String name );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );

}
