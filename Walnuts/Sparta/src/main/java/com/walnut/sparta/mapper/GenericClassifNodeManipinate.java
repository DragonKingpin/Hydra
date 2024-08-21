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
    @Insert("INSERT INTO `hydra_classif_node` (`uuid`, `name`, `rules_uuid`) VALUES (#{UUID},#{name},#{rulesUUID})")
    void saveClassifNode(GenericClassificationNode classificationNode);
    @Delete("DELETE FROM `hydra_classif_node` WHERE `uuid`=#{UUID}")
    void deleteClassifNode(@Param("UUID")GUID UUID);
    @Select("SELECT `id`, `uuid`, `name`, `rules_uuid` AS rulesUUID FROM `hydra_classif_node` WHERE `uuid`=#{UUID}")
    GenericClassificationNode selectClassifNode(@Param("UUID")GUID UUID);
    void updateClassifNode(GenericClassificationNode classificationNode);
    @Select("SELECT `id`, `UUID`, `name`, `rules_uuid` FROM `hydra_classif_node` WHERE name=#{name}")
    List<GenericClassificationNode> selectClassifNodeByName(@Param("name") String name);
}
