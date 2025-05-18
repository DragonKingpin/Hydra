package com.pinecone.hydra.deploy.ibatis;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.entity.GenericVirtualMachine;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.deploy.kom.source.VirtualMachineManipulator;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface VirtualMachineMapper extends VirtualMachineManipulator {

    @Insert("INSERT INTO `hydra_deploy_virtual_machine` (`guid`, `name`, `ip_address`, `status`,`affiliate_host_guid`) VALUES (#{guid},#{name},#{ipAddress},#{status},#{affiliateHostGuid})")
    void insert( VirtualMachineElement virtualMachineElement );

    @Insert( "UPDATE `hydra_deploy_virtual_machine` SET `name` = #{name}, `ip_address` = #{ipAddress}, `status` = #{status}, `affiliate_host_guid` = #{affiliateHostGuid} WHERE `guid` = #{guid}")
    void update( VirtualMachineElement serviceElement );

    @Delete("DELETE FROM `hydra_deploy_virtual_machine` WHERE `guid` = #{guid}")
    void remove( GUID guid );

    @Select("SELECT `guid`, `name`, `ip_address` as ipAddress, `status`, `affiliate_host_guid` FROM `hydra_deploy_virtual_machine` WHERE `guid` = #{guid}")
    GenericVirtualMachineElement getDeployNode0( GUID guid );

    @Override
    default VirtualMachineElement getDeployNode( GUID guid, DeployInstrument instrument ){
        GenericVirtualMachineElement element = this.getDeployNode0( guid );
        element.apply( instrument );
        return element;
    }

    @Override
    @Select( "SELECT `guid` FROM `hydra_deploy_virtual_machine` WHERE `name` = #{name}" )
    List<GUID> getGuidsByName( String name );

    @Override
    @Select( "SELECT `guid` FROM `hydra_deploy_virtual_machine` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );


}