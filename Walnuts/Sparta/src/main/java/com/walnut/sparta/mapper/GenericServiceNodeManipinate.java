package com.walnut.sparta.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericServiceNode;
import com.pinecone.hydra.unit.udsn.ServiceNodeManipinate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.UUID;

@Mapper
public interface GenericServiceNodeManipinate extends ServiceNodeManipinate {

    @Insert("INSERT INTO `hydra_service_node` (`uuid`, `name`) VALUES (#{UUID},#{name})")
    void saveServiceNode(GenericServiceNode serviceNode);
    @Delete("DELETE FROM `hydra_service_node` WHERE `uuid`=#{UUID}")
    void deleteServiceNode(@Param("UUID")GUID UUID);
    @Select("SELECT `id`, `UUID`, `name` FROM `hydra_service_node` WHERE `UUID`=#{UUID}")
    GenericServiceNode selectServiceNode(@Param("UUID") GUID UUID);
    void updateServiceNode(GenericServiceNode serviceNode);
    @Select("SELECT `id`, `UUID`, `name` FROM `hydra_service_node` WHERE name=#{name}")
    List<GenericServiceNode> selectServiceNodeByName(@Param("name") String name);
}
