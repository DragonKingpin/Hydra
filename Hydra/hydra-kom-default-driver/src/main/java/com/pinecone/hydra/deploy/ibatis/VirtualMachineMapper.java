package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.hydra.deploy.entity.GenericVirtualMachine;
import com.pinecone.hydra.deploy.kom.source.VirtualMachineManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@IbatisDataAccessObject
public interface VirtualMachineMapper extends VirtualMachineManipulator {
    @Insert("INSERT INTO `hydra_deploy_virtual_machine` (`guid`, `name`, `ip_address`, `status`,`affiliate_host_guid`) VALUES (#{guid},#{name},#{ipAddress},#{status},#{affiliateHostGuid})")
    void insert( GenericVirtualMachine virtualMachine );
}
