package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployFamilyNode;
import com.pinecone.hydra.deploy.kom.entity.CommonMeta;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.source.NodeMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface DeployNodeMetaMapper extends NodeMetaManipulator {
    @Insert( "INSERT INTO `hydra_deploy_node_meta` (`guid`,`description`,`extra_information`) VALUES (#{guid}, #{description}, #{extraInformation})")
    void insert( DeployFamilyNode node );

    void insertNS( Namespace node );

    @Delete("DELETE FROM `hydra_deploy_node_meta` WHERE `guid`=#{guid}")
    void remove( GUID guid );

    @Select("SELECT `id` AS `enumId`, `guid`, `description` AS Description, `extra_information` AS ExtraInformation FROM `hydra_deploy_node_meta` WHERE `guid` = #{guid}")
    CommonMeta getNodeCommonMeta( @Param("guid") GUID guid );

    void update( DeployFamilyNode node );


}
