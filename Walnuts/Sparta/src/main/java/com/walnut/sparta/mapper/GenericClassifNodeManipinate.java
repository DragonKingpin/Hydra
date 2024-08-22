package com.walnut.sparta.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.unit.udsn.ClassifNodeManipinate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface GenericClassifNodeManipinate extends ClassifNodeManipinate {
    @Insert("INSERT INTO `hydra_classif_node` (`guid`, `name`, `rules_guid`) VALUES (#{UUID},#{name},#{rulesUUID})")
    void saveClassifNode(GenericClassificationNode classificationNode);
    @Delete("DELETE FROM `hydra_classif_node` WHERE `guid`=#{UUID}")
    void deleteClassifNode(@Param("UUID")GUID UUID);
    @Select("SELECT `id`, `guid` AS UUID, `name`, `rules_guid` AS rulesUUID FROM `hydra_classif_node` WHERE `guid`=#{UUID}")
    GenericClassificationNode selectClassifNode(@Param("UUID")GUID UUID);
    void updateClassifNode(GenericClassificationNode classificationNode);
    @Select("SELECT `id`, `guid` AS UUID, `name`, `rules_guid` AS rulesUUID FROM `hydra_classif_node` WHERE name=#{name}")
    List<GenericClassificationNode> selectClassifNodeByName(@Param("name") String name);
}
