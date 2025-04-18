package com.pinecone.hydra.atlas.runtime.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface RuntimeVectorGraphPathCacheMapper extends VectorGraphPathCacheManipulator {
    @Override
    @Insert("INSERT INTO hydra_atlas_vgraph_cache_path (`guid`, `path`) VALUES (#{path},#{guid})")
    void insert(@Param("path") String path, @Param("guid") GUID guid);

    @Override
    @Insert("INSERT INTO hydra_atlas_vgraph_cache_path (guid, path, long_path) VALUES (#{guid},#{path},#{longPath})")
    void insertLongPath( GUID guid, String path, String longPath );

    @Override
    @Delete("DELETE FROM `hydra_atlas_vgraph_cache_path` WHERE `guid` = #{guid}")
    void remove ( GUID guid );

    @Override
    @Select("SELECT `path` FROM `hydra_atlas_vgraph_cache_path` WHERE `guid` = #{guid}")
    List<String> getPath (GUID guid );

    @Override
    @Select("SELECT `guid` FROM `hydra_atlas_vgraph_cache_path` WHERE `path` = #{path}")
    GUID getNode ( String path );

    @Override
    @Select("SELECT `guid` FROM `hydra_atlas_vgraph_cache_path` WHERE `path` = #{path}")
    GUID queryGUIDByPath( String path );
}
