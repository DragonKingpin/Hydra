package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericQuickElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.deploy.kom.source.QuickElementManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface QuickElementMapper extends QuickElementManipulator {

    @Insert("INSERT INTO `hydra_deploy_quick` (`guid`, `type_name`, `enable`) VALUES (#{guid},#{typeName},#{enable})")
    void insert(GenericQuickElement quickElement );

    @Insert("UPDATE `hydra_deploy_quick` SET `enable` = #{enable} , `type_name` = #{typeName} WHERE `guid` = #{guid}")
    void update(VirtualMachineElement serviceElement);

    @Delete("DELETE FROM `hydra_deploy_quick` WHERE `guid` = #{guid}")
    void remove(GUID guid);

    @Select("SELECT `guid`, `type_name` AS TypeName, `enable` AS Enable FROM `hydra_deploy_quick` WHERE `guid` = #{guid}")
    GenericQuickElement getDeployNode(GUID guid, DeployInstrument deployInstrument);
}
