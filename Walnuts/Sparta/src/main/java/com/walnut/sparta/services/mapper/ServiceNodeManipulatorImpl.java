package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ServiceNodeManipulatorImpl extends ServiceNodeManipulator {

    @Insert("INSERT INTO `hydra_service_node` (`guid`, `name`) VALUES (#{guid},#{name})")
    void saveServiceNode(GenericServiceNode serviceNode);
    @Delete("DELETE FROM `hydra_service_node` WHERE `guid`=#{guid}")
    void deleteServiceNode(@Param("guid")GUID guid);
    @Select("SELECT `id`, `guid`, `name` FROM `hydra_service_node` WHERE `guid`=#{guid}")
    GenericServiceNode selectServiceNode(@Param("guid") GUID guid);
    void updateServiceNode(GenericServiceNode serviceNode);
    @Select("SELECT `id`, `guid` , `name` FROM `hydra_service_node` WHERE name=#{name}")
    List<GenericServiceNode> selectServiceNodeByName(@Param("name") String name);
}
