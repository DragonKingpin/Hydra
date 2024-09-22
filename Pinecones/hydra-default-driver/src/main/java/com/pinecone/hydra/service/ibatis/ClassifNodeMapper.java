package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ClassifNodeMapper extends ClassifNodeManipulator {
    @Insert("INSERT INTO `hydra_service_classification_node` (`guid`, `name`, `rules_guid`) VALUES (#{guid},#{name},#{rulesGUID})")
    void insert(GenericClassificationNode classificationNode);

    @Delete("DELETE FROM `hydra_service_classification_node` WHERE `guid`=#{guid}")
    void remove(@Param("guid")GUID GUID);

    @Select("SELECT `id`, `guid`, `name`, `rules_guid` AS rulesGUID FROM `hydra_service_classification_node` WHERE `guid`=#{guid}")
    GenericClassificationNode getClassifNode(@Param("guid") GUID guid);

    void update(GenericClassificationNode classificationNode);

    @Select("SELECT `id`, `guid`, `name`, `rules_guid` AS rulesGUID FROM `hydra_service_classification_node` WHERE name=#{name}")
    List<GenericClassificationNode> fetchClassifNodeByName(@Param("name") String name);
}
