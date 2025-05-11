package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.hydra.deploy.entity.GenericPhysicalHost;
import com.pinecone.hydra.deploy.kom.source.PhysicalHostManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@IbatisDataAccessObject
public interface PhysicalHostMapper extends PhysicalHostManipulator {

    @Insert("INSERT INTO `hydra_deploy_physical_host` (`guid`, `name`, `ip_address`, `hardware_specs`, `status`) VALUES (#{guid},#{name},#{ipAddress},#{hardwareSpecs},#{status})")
    void insert(GenericPhysicalHost physicalHost);

}
