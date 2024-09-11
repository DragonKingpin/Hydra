package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.source.ConfNodePathManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ConfNodePathMapper extends ConfNodePathManipulator {
    @Insert("INSERT INTO `hydra_conf_node_path` (`path`, `guid`) VALUES (#{path},#{guid})")
    void insert(@Param("guid") GUID guid, @Param("path") String path);
    @Delete("DELETE FROM `hydra_conf_node_path` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `path` FROM `hydra_conf_node_path` WHERE `guid`=#{guid}")
    String getPath(GUID guid);
    @Select("SELECT `guid` FROM `hydra_conf_node_path` WHERE `guid`=#{guid}")
    GUID getNode(String path);
}
