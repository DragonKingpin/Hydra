package com.pinecone.hydra.layer.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.layer.LayerNamespace;
import com.pinecone.hydra.unit.vgraph.layer.source.NamespaceManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface NamespaceMapper extends NamespaceManipulator {
    @Insert("INSERT INTO `hydra_atlas_layer_namespace` (`guid`, `name`, `update_time`, `create_time`) VALUES (#{guid},#{name},#{updateTime},#{createTime})")
    void insert( LayerNamespace layerNamespace );

    @Delete("DELETE FROM `hydra_atlas_layer_namespace` WHERE guid = #{guid}")
    void remove( GUID guid );

    @Select("SELECT `id` AS enumId, `guid`, `name`, `update_time` AS updateTime, `create_time` AS createTime FROM `hydra_atlas_layer_namespace` WHERE `guid` = #{guid}")
    LayerNamespace query( GUID guid );

    @Select( "SELECT `guid` FROM `hydra_atlas_layer_namespace` WHERE `name` = #{name}" )
    List<GUID > getGuidsByName(String name );

    @Select( "SELECT `guid` FROM `hydra_atlas_layer_namespace` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID > getGuidsByNameID(@Param("name") String name, @Param("guid") GUID guid );
}
