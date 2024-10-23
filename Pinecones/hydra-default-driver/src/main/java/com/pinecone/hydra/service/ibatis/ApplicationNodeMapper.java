package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ApplicationNodeMapper extends ApplicationNodeManipulator {
    @Insert("INSERT INTO  `hydra_service_application_node` (`guid`, `name`) VALUES (#{guid},#{name})")
    void insert(GenericApplicationNode applicationNode);

    @Delete("DELETE FROM `hydra_service_application_node` WHERE `guid`=#{guid}")
    void remove(@Param("guid")GUID guid);

    @Select("SELECT `id` AS `enumId`, `guid`, `name` FROM `hydra_service_application_node` WHERE `guid`=#{guid}")
    GenericApplicationNode getApplicationNode(@Param("guid")GUID guid);

    void update(GenericApplicationNode applicationNode);

    @Override
    @Select( "SELECT `guid` FROM `hydra_service_application_node` WHERE `name` = #{name}" )
    List<GUID> getGuidsByName(String name);

    @Override
    @Select( "SELECT `guid` FROM `hydra_service_application_node` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID> getGuidsByNameID(String name, GUID guid);
}
