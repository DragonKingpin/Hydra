package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.entity.DeployInsMapping;
import com.pinecone.hydra.deploy.kom.entity.GenericDeployInsMapping;
import com.pinecone.hydra.deploy.kom.source.DeployServiceInsMappingManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface DeployServiceInsMappingMapper extends DeployServiceInsMappingManipulator {
    @Override
    @Insert("INSERT INTO `hydra_deploy_service_ins_mapping` (`deploy_guid`, `service_ins_guid`) VALUES (#{deployGuid}, #{serviceInsGuid})")
    void insert(@Param("deployInsMapping") DeployInsMapping deployInsMapping);

    @Override
    @Select("SELECT `id`, `deploy_guid`, `service_ins_guid`, `create_time`, `update_time` " +
            "FROM `hydra_deploy_service_ins_mapping` WHERE `service_ins_guid` = #{insGuid}")
    GenericDeployInsMapping queryDeployInsMappingByInsGuid(@Param("insGuid") GUID insGuid);

    @Override
    @Select("SELECT `id`, `deploy_guid`, `service_ins_guid`, `create_time`, `update_time` " +
            "FROM `hydra_deploy_service_ins_mapping` WHERE `deploy_guid` = #{deployGuid}")
    GenericDeployInsMapping queryDeployInsMappingByDeployGuid(GUID deployGuid);

    @Override
    @Delete("DELETE FROM `hydra_deploy_service_ins_mapping` WHERE `service_ins_guid` = #{insGuid}")
    void removeByInsGuid(GUID insGuid);

    @Override
    @Delete("DELETE FROM `hydra_deploy_service_ins_mapping` WHERE `deploy_guid` = #{deployGuid}")
    void removeByDeployGuid(GUID deployGuid);
}
