package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ServiceNodeMapper extends ServiceNodeManipulator {

    @Insert("INSERT INTO `hydra_service_service_node` (`guid`, `name`) VALUES (#{guid},#{name})")
    void insert(GenericServiceNode serviceNode);
    @Delete("DELETE FROM `hydra_service_service_node` WHERE `guid`=#{guid}")
    void remove(@Param("guid")GUID guid);
    @Select("SELECT `id` AS `enumId`, `guid`, `name` FROM `hydra_service_service_node` WHERE `guid`=#{guid}")
    GenericServiceNode getServiceNode(@Param("guid") GUID guid);
    void updateServiceNode(GenericServiceNode serviceNode);
    @Select("SELECT `id` AS `enumId`, `guid` , `name` FROM `hydra_service_service_node` WHERE name=#{name}")
    List<GenericServiceNode> fetchServiceNodeByName(@Param("name") String name);
}
