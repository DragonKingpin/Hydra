package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.nodes.GenericNamespace;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ServiceNamespaceMapper extends ServiceNamespaceManipulator {
    @Insert("INSERT INTO `hydra_service_namespace_node` (`guid`, `name`, `rules_guid`) VALUES (#{guid},#{name},#{rulesGUID})")
    void insert(GenericNamespace classificationNode);

    @Delete("DELETE FROM `hydra_service_namespace_node` WHERE `guid`=#{guid}")
    void remove(@Param("guid")GUID GUID);

    @Select("SELECT `id` AS `enumId`, `guid`, `name`, `rules_guid` AS rulesGUID FROM `hydra_service_namespace_node` WHERE `guid`=#{guid}")
    GenericNamespace getNamespace(@Param("guid") GUID guid);

    void update(GenericNamespace classificationNode);

    @Select("SELECT `id` AS `enumId`, `guid`, `name`, `rules_guid` AS rulesGUID FROM `hydra_service_namespace_node` WHERE name=#{name}")
    List<GenericNamespace> fetchNamespaceNodeByName(@Param("name") String name);

    @Override
    @Select( "SELECT `guid` FROM `hydra_service_namespace_node` WHERE `name` = #{name}" )
    List<GUID> getGuidsByName(String name);

    @Override
    @Select( "SELECT `guid` FROM `hydra_service_namespace_node` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID> getGuidsByNameID(String name, GUID guid);
}
