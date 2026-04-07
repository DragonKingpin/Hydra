package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.entity.GenericPhysicalHost;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.GenericQuickElement;
import com.pinecone.hydra.deploy.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.deploy.kom.source.PhysicalHostManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface PhysicalHostMapper extends PhysicalHostManipulator {

    @Override
    @Insert("INSERT INTO `hydra_deploy_physical_host` (`guid`, `name`, `hardware_specs`, `status`) VALUES (#{guid},#{name},#{hardwareSpecs},#{status})")
    void insert( PhysicalHostElement physicalHostElement );

    @Override
    @Insert("UPDATE `hydra_deploy_physical_host` SET `name` = #{name},  `hardware_specs` = #{hardwareSpecs}, `status` = #{status} WHERE `guid` = #{guid}")
    void update( PhysicalHostElement serviceElement );

    @Override
    @Delete("DELETE FROM `hydra_deploy_physical_host` WHERE `guid` = #{guid}")
    void remove(GUID guid);

    @Select("SELECT `guid`, `name` as ipAddress, `hardware_specs` as hardwareSpecs, `status` FROM `hydra_deploy_physical_host` WHERE `guid` = #{guid}")
    GenericPhysicalHostElement getPhysicalHostElement0( GUID guid );

    @Override
    default GenericPhysicalHostElement getPhysicalHostElement( GUID guid, DeployInstrument instrument ){
        GenericPhysicalHostElement element = this.getPhysicalHostElement0( guid );
        element.apply( instrument );
        return element;
    }

    @Select("SELECT `guid` FROM `hydra_deploy_physical_host` WHERE `name`=#{name}")
    @Override
    List<GUID> getGuidsByName(String name );

    @Select("SELECT `guid` FROM `hydra_deploy_physical_host` WHERE `name`=#{name} AND `guid`!=#{guid}")
    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );

}
