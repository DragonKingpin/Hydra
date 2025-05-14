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

@Mapper
@IbatisDataAccessObject
public interface PhysicalHostMapper extends PhysicalHostManipulator {

    @Insert("INSERT INTO `hydra_deploy_physical_host` (`guid`, `name`, `ip_address`, `hardware_specs`, `status`) VALUES (#{guid},#{name},#{ipAddress},#{hardwareSpecs},#{status})")
    void insert(PhysicalHostElement physicalHostElement);

    @Insert("UPDATE `hydra_deploy_physical_host` SET `name` = #{name}, `ip_address` = #{ipAddress}, `hardware_specs` = #{hardwareSpecs}, `status` = #{status} WHERE `guid` = #{guid}")
    void update(PhysicalHostElement serviceElement);

    @Delete("DELETE FROM `hydra_deploy_physical_host` WHERE `guid` = #{guid}")
    void remove(GUID guid);

   @Select("SELECT `guid`, `name`, `ip_address`, `hardware_specs`, `status` FROM `hydra_deploy_physical_host` WHERE `guid` = #{guid}")
   GenericPhysicalHostElement getDeployNode(GUID guid, DeployInstrument deployInstrument);

}
