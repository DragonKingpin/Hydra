package com.pinecone.hydra.layer.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerOwnerManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@IbatisDataAccessObject
public interface LayerOwnerMapper extends LayerOwnerManipulator {
    @Insert("INSERT INTO `hydra_layer_tree` (`guid`) VALUES ( #{guid} )")
    void insertRootNode(@Param("guid") GUID guid );

    @Insert( "INSERT INTO `hydra_layer_tree` (`guid`, `parent_guid`) VALUES (#{targetGuid}, #{parentGuid})" )
    void insert( @Param("targetGuid") GUID targetGuid, @Param("parentGuid") GUID parentGUID );


    @Update( "UPDATE `hydra_layer_tree` SET `guid` = #{targetGuid}, `parent_guid` = #{parentGuid} WHERE `guid` = #{targetGuid}" )
    void update( @Param("targetGuid") GUID targetGuid, @Param("parentGuid") GUID parentGUID );

    @Update( "UPDATE `hydra_layer_tree` SET `guid` = #{targetGuid}, `parent_guid` = #{parentGuid} WHERE `guid` = #{targetGuid}" )
    void updateParentGuid( @Param("targetGuid") GUID targetGuid, @Param("parentGuid") GUID parentGUID );

    @Delete( "DELETE FROM `hydra_layer_tree` WHERE `guid`=#{subordinateGuid} " )
    void remove( @Param("subordinateGuid") GUID subordinateGuid );

    @Delete( "DELETE FROM `hydra_layer_tree` WHERE `guid`=#{subordinateGuid} " )
    void removeBySubordinate( GUID subordinateGuid );
}
