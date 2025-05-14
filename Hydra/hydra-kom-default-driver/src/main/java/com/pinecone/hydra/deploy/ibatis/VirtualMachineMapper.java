package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.entity.GenericVirtualMachine;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.deploy.kom.source.VirtualMachineManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface VirtualMachineMapper extends VirtualMachineManipulator {
    @Insert("INSERT INTO `hydra_deploy_virtual_machine` (`guid`, `name`, `ip_address`, `status`,`affiliate_host_guid`) VALUES (#{guid},#{name},#{ipAddress},#{status},#{affiliateHostGuid})")
    void insert( GenericVirtualMachineElement virtualMachineElement );

    @Insert( "UPDATE `hydra_deploy_virtual_machine` SET `name` = #{name}, `ip_address` = #{ipAddress}, `status` = #{status}, `affiliate_host_guid` = #{affiliateHostGuid} WHERE `guid` = #{guid}")
    void update(GenericVirtualMachineElement serviceElement);

    @Delete("DELETE FROM `hydra_deploy_virtual_machine` WHERE `guid` = #{guid}")
    void remove(GUID guid);

    @Select("SELECT `guid`, `name`, `ip_address`, `status`, `affiliate_host_guid` FROM `hydra_deploy_virtual_machine` WHERE `guid` = #{guid}")
    GenericVirtualMachineElement getDeployNode(GUID guid, DeployInstrument instrument);


}