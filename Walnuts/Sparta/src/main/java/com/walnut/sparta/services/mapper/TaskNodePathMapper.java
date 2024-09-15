package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TaskNodePathMapper extends ScopePathManipulator {
    @Insert("INSERT INTO `hydra_task_node_path` (`path`, `guid`) VALUES (#{path},#{guid})")
    void insert(@Param("guid") GUID guid, @Param("path") String path);
    @Delete("DELETE FROM `hydra_task_node_path` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `path` FROM `hydra_task_node_path` WHERE `guid`=#{guid}")
    String getPath(GUID guid);
    @Select("SELECT `guid` FROM `hydra_task_node_path` WHERE `path`=#{path}")
    GUID getNode(String path);
}
