package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.GenericLocalCluster;
import com.pinecone.hydra.storage.file.entity.LocalCluster;
import com.pinecone.hydra.storage.file.source.LocalClusterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface LocalClusterMapper extends LocalClusterManipulator {
    LocalCluster getLocalCluster(GUID guid, ElementNode element);
    @Insert("INSERT INTO `hydra_uofs_local_cluster_fat` (`file_guid`, `seg_guid`, `seg_id`, `create_time`, `update_time`, `source_name`, `crc32`, `size`) VALUES (#{fileGuid},#{segGuid},#{segId},#{createTime},#{updateTime},#{sourceName},#{crc32},#{size})")
    void insert( LocalCluster localCluster );
    @Delete("DELETE FROM `hydra_uofs_local_cluster_fat` WHERE `seg_guid` = #{guid}")
    void remove( GUID guid );
    @Delete("DELETE FROM `hydra_uofs_local_cluster_fat` WHERE `file_guid` = #{fileGuid}")
    void removeClustersByFile( GUID fileGuid );
    default GenericLocalCluster getLocalClusterByGuid(GUID guid){
        GenericLocalCluster localCluster = this.getLocalClusterByGuid0(guid);
        if ( localCluster == null ) return null;
        localCluster.setLocalClusterManipulator( this );
        return localCluster;
    }
    @Select("SELECT `id` AS enumId, `file_guid` AS fileGuid, `seg_guid` AS segGuid, `seg_id` AS segId, `create_time` AS createTime, `update_time` AS updateTime, `source_name` AS sourceName, `crc32`, `size` FROM `hydra_uofs_local_cluster_fat` WHERE `seg_guid` = #{guid}")
    GenericLocalCluster getLocalClusterByGuid0(GUID guid);
    @Select("SELECT `id`, `file_guid` AS fileGuid, `seg_guid` AS segGuid, `seg_id` AS segId, `create_time` AS createTime, `update_time` AS updateTime, `source_name` AS sourceName, `crc32`, `size` FROM `hydra_uofs_local_cluster_fat` WHERE `file_guid` = #{guid}")
    List<GenericLocalCluster> getLocalClusterByFileGuid0(GUID guid );

    @Select("SELECT `id`, `file_guid` AS fileGuid, `seg_guid` AS segGuid, `seg_id` AS segId, `create_time` AS createTime, `update_time` AS updateTime, `source_name` AS sourceName, `crc32`, `size` FROM `hydra_uofs_local_cluster_fat` WHERE `file_guid` = #{fileGuid} AND `seg_id` = #{segId}")
    GenericLocalCluster getClusterByFileWithId0(GUID fileGuid, long segId );

    @Update("UPDATE `hydra_uofs_local_cluster_fat` SET `size` = #{size} WHERE `file_guid` = #{fileGuid} AND `seg_id` = #{segId}")
    void update( LocalCluster localCluster );

    @Delete("DELETE FROM `hydra_uofs_local_cluster_fat` WHERE file_guid = #{fileGuid} AND seg_id = #{segId}")
    void removeClusterByFileWithId( GUID fileGuid, long segId );

    default GenericLocalCluster getClusterByFileWithId(GUID fileGuid, long segId ){
        GenericLocalCluster frame = this.getClusterByFileWithId0(fileGuid, segId);
        if( frame == null ){
            return null;
        }
        frame.setLocalClusterManipulator( this );
        return frame;
    }
    default List<LocalCluster> getLocalClusterByFileGuid(GUID guid){
        List<LocalCluster> localClusters = new ArrayList<>();
        List<GenericLocalCluster> frames = this.getLocalClusterByFileGuid0(guid);
        for (LocalCluster frame : frames){
            frame.setLocalClusterManipulator(this);
            localClusters.add( frame );
        }
        return localClusters;
    }
}
