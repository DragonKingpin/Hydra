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
public interface ApplicationNodeManipulatorImpl extends ApplicationNodeManipulator {
    @Insert("INSERT INTO  `hydra_application_node` (`guid`, `name`) VALUES (#{guid},#{name})")
    void insert(GenericApplicationNode applicationNode);
    @Delete("DELETE FROM `hydra_application_node` WHERE `guid`=#{UUID}")
    void delete(@Param("guid")GUID guid);
    @Select("SELECT `id`, `guid`, `name` FROM `hydra_application_node` WHERE `guid`=#{guid}")
    GenericApplicationNode getApplicationNode(@Param("guid")GUID guid);
    void update(GenericApplicationNode applicationNode);
    @Select("SELECT `id`, `guid`, `name` FROM `hydra_application_node` WHERE name=#{name}")
    List<GenericApplicationNode> fetchApplicationNodeByName(@Param("name") String name);
}
