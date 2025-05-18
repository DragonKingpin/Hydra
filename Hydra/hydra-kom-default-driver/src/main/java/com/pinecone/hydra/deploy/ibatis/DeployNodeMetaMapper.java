package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployFamilyNode;
import com.pinecone.hydra.deploy.kom.entity.GenericCommonMeta;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.source.NodeMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface DeployNodeMetaMapper extends NodeMetaManipulator {

    @Override
    @Insert( "INSERT INTO `hydra_deploy_node_meta` (`guid`,`description`,`extra_information`,`name`) VALUES (#{guid}, #{description}, #{extraInformation},#{name})")
    void insert( DeployFamilyNode node );

    @Override
    @Insert( "INSERT INTO `hydra_deploy_node_meta` (`guid`,`description`,`extra_information`,`name`) VALUES (#{guid}, #{description}, #{extraInformation},#{name})")
    void insertNS( Namespace node );

    @Override
    @Delete("DELETE FROM `hydra_deploy_node_meta` WHERE `guid`=#{guid}")
    void remove( GUID guid );

    @Override
    @Select("SELECT `id` AS `enumId`, `guid`, `description` AS Description, `extra_information` AS ExtraInformation FROM `hydra_deploy_node_meta` WHERE `guid` = #{guid}")
    GenericCommonMeta getNodeCommonMeta(@Param("guid") GUID guid );

    @Override
    @Update( "UPDATE `hydra_deploy_node_meta` SET `description` = #{description} , `extra_information` = #{extraInformation} WHERE guid = #{guid}")
    void update( DeployFamilyNode node );


}
