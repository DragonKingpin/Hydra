package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericQuickElement;
import com.pinecone.hydra.deploy.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.deploy.kom.source.QuickElementManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface QuickElementMapper extends QuickElementManipulator {

    @Override
    @Insert("INSERT INTO `hydra_deploy_quick` (`guid`, `type_name`, `enable`,`name`) VALUES (#{guid},#{typeName},#{enable},#{name})")
    void insert( QuickElement quickElement );

    @Override
    @Insert("UPDATE `hydra_deploy_quick` SET `enable` = #{enable} , `type_name` = #{typeName} ,`name` = #{name} WHERE  `guid` = #{guid}")
    void update( QuickElement serviceElement );

    @Override
    @Delete("DELETE FROM `hydra_deploy_quick` WHERE `guid` = #{guid}")
    void remove( GUID guid );

    @Select("SELECT `guid`, `type_name` AS typeName, `enable` AS Enable FROM `hydra_deploy_quick` WHERE `guid` = #{guid}")
    GenericQuickElement getQuickElement0( GUID guid );

    @Override
    default GenericQuickElement getQuickElement( GUID guid, DeployInstrument instrument ){
        GenericQuickElement element = this.getQuickElement0( guid );
        element.apply( instrument );
        return element;
    }


    @Override
    @Select("SELECT `guid`, `type_name` AS typeName, `enable` AS enable ,`name` AS Name FROM `hydra_deploy_quick` WHERE `guid` = #{guid}")
    List<GUID > getGuidsByName(String name );

    @Override
    @Select("SELECT `guid`, `type_name` AS typeName, `enable` AS enable ,`name` AS Name FROM `hydra_deploy_quick` WHERE `guid` = #{guid} AND `name` = #{name}")
    List<GUID > getGuidsByNameID( String name, GUID guid );


    @Select( "SELECT `guid`, `type_name` AS typeName, `enable` AS enable ,`name` AS Name FROM `hydra_deploy_quick` WHERE `name` = #{name}")
    List<QuickElement> fetchQuickElementByName(@Param("name") String name );
}
