package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ContainerElement;
import com.pinecone.hydra.deploy.kom.entity.GenericContainerElement;
import com.pinecone.hydra.deploy.kom.entity.GenericQuickElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.source.ContainerElementManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
@IbatisDataAccessObject
public interface ContainerElementMapper extends ContainerElementManipulator {

    @Override
    @Insert("INSERT INTO `hydra_deploy_container` (`guid`, `status`,`name`) VALUES (#{guid},#{status},#{name})")
    void insert( ContainerElement quickElement );

    @Override
    @Insert("UPDATE `hydra_deploy_container` SET `status` = #{status} ,`name` = #{name} WHERE  `guid` = #{guid}")
    void update( ContainerElement serviceElement );

    @Override
    @Delete("DELETE FROM `hydra_deploy_container` WHERE `guid` = #{guid}")
    void remove( GUID guid );

    @Select("SELECT `guid`, `status` AS status FROM `hydra_deploy_container` WHERE `guid` = #{guid}")
    GenericContainerElement getContainerElement0( GUID guid );

    @Override
    default GenericContainerElement getContainerElement(GUID guid, DeployInstrument instrument ){
        GenericContainerElement element = this.getContainerElement0( guid );
        element.apply( instrument );
        return element;
    }


    @Override
    @Select("SELECT `guid`, `status` AS status,`name` AS name FROM `hydra_deploy_container` WHERE `guid` = #{guid}")
    List<GUID > getGuidsByName(String name );

    @Override
    @Select("SELECT `guid`, `status` AS status,`name` AS name FROM `hydra_deploy_container` WHERE `guid` = #{guid} AND `name` = #{name}")
    List<GUID > getGuidsByNameID( String name, GUID guid );


    @Select( "SELECT `guid`, `status` AS status,`name` AS name FROM `hydra_deploy_container` WHERE `name` = #{name}")
    List<QuickElement> fetchQuickElementByName(@Param("name") String name );
}
