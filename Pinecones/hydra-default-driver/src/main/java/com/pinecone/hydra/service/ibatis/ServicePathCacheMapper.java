package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.source.TriePathCacheManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
@IbatisDataAccessObject
public interface ServicePathCacheMapper extends TriePathCacheManipulator {
    @Insert("INSERT INTO `hydra_service_node_path` (`guid`, `path`) VALUES (#{guid},#{path})")
    void insert(@Param("guid") GUID guid, @Param("path") String path);
    @Delete("DELETE FROM `hydra_service_node_path` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `path` FROM `hydra_service_node_path` WHERE `guid`=#{guid}")
    String getPath(GUID guid);
    @Select("SELECT `guid` FROM `hydra_service_node_path` WHERE `path`=#{path}")
    GUID getNode(String path);
}
