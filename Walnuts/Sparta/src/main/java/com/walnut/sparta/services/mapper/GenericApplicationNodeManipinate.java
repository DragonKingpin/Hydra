package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface GenericApplicationNodeManipinate extends ApplicationNodeManipulator {
    @Insert("INSERT INTO  `hydra_application_node` (`guid`, `name`) VALUES (#{UUID},#{name})")
    void saveApplicationNode(GenericApplicationNode applicationNode);
    @Delete("DELETE FROM `hydra_application_node` WHERE `guid`=#{UUID}")
    void deleteApplicationNode(@Param("UUID")GUID UUID);
    @Select("SELECT `id`, `guid` AS UUID, `name` FROM `hydra_application_node` WHERE `guid`=#{UUID}")
    GenericApplicationNode selectApplicationNode(@Param("UUID")GUID UUID);
    void updateApplicationNode(GenericApplicationNode applicationNode);
    @Select("SELECT `id`, `guid` AS UUID, `name` FROM `hydra_application_node` WHERE name=#{name}")
    List<GenericApplicationNode> selectApplicationNodeByName(@Param("name") String name);
}
