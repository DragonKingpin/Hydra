package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.Cluster;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.GenericRemoteCluster;
import com.pinecone.hydra.storage.file.entity.RemoteCluster;
import com.pinecone.hydra.storage.file.source.RemoteClusterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface RemoteClusterMapper extends RemoteClusterManipulator {
    RemoteCluster getRemoteCluster(GUID guid, ElementNode element);
    @Insert("INSERT INTO `hydra_uofs_files_cluster_mapping` (`file_guid`, `seg_guid`, `device_guid`, `seg_id`, `crc32`, `size`) VALUES (#{fileGuid},#{segGuid},#{deviceGuid},#{segId},#{crc32},#{size})")
    void insert( RemoteCluster remoteCluster );
    @Delete("DELETE FROM `hydra_uofs_files_cluster_mapping` WHERE `seg_guid` = #{guid}")
    void remove( GUID guid );
    @Delete("DELETE FROM `hydra_uofs_files_cluster_mapping` WHERE file_guid = #{fileGuid}")
    void removeClustersByFile( GUID fileGuid );
    @Select("SELECT `id` AS enumID, `file_guid` AS fileGuid, `seg_guid` AS segGuid, `device_guid` AS deviceGuid, `seg_id` AS segId, `crc32`, `size` FROM `hydra_uofs_files_cluster_mapping` WHERE `seg_guid` = #{guid}")
    RemoteCluster fetchRemoteClustersByFileGuid(GUID guid);
    @Select("SELECT `id`, `file_guid` AS fileGuid, `seg_guid` AS segGuid, `device_guid` AS deviceGuid, `seg_id` AS segId, `crc32`, `size` FROM `hydra_uofs_files_cluster_mapping` WHERE `file_guid` = #{guid}")
    List<GenericRemoteCluster> fetchRemoteClustersByFileGuid0( GUID guid );

    @Override
    default List<RemoteCluster> fetchRemoteClusterByFileGuid( GUID guid ){
        List<RemoteCluster> remoteClusters = new ArrayList<>();
        List<GenericRemoteCluster> frames = this.fetchRemoteClustersByFileGuid0(guid);
        for (RemoteCluster frame : frames){
            frame.setRemoteClusterManipulator(this);
            remoteClusters.add(frame);
        }
        return remoteClusters;
    };

    @Select("SELECT `id`, `file_guid` AS fileGuid, `seg_guid` AS segGuid, `device_guid` AS deviceGuid, `seg_id` AS segId, `crc32`, `size` " +
            "FROM `hydra_uofs_files_cluster_mapping` " +
            "WHERE `file_guid` = #{guid} " +
            "ORDER BY `seg_id`, `id` ASC " +
            "LIMIT #{offset}, #{pageSize}")
    List<GenericRemoteCluster> fetchRemoteClusterByFileGuidPart0(
            @Param("guid") GUID guid,
            @Param("offset") long offset,
            @Param("pageSize") int pageSize);

    @Override
    default List<RemoteCluster > fetchRemoteClusterByFileGuid( GUID guid, long offset, int pageSize ) {
        List<RemoteCluster> remoteClusters = new ArrayList<>();
        List<GenericRemoteCluster> frames = this.fetchRemoteClusterByFileGuidPart0( guid, offset, pageSize );
        for ( RemoteCluster frame : frames ){
            frame.setRemoteClusterManipulator( this );
            remoteClusters.add(frame);
        }
        return remoteClusters;
    };

    @Select("SELECT COUNT(*) FROM `hydra_uofs_files_cluster_mapping` WHERE `file_guid` = #{guid}")
    long countRemoteClustersByFileGuid( @Param("guid") GUID guid );



    @Select("SELECT `id` AS emunId, `file_guid` AS fileGuid, `seg_guid` AS segGuid, `device_guid` AS deviceGuid, `seg_id` AS segId, `crc32`, `size` FROM `hydra_uofs_files_cluster_mapping` WHERE `file_guid` = #{guid} ORDER BY `seg_id` DESC LIMIT 1")
    RemoteCluster getLastCluster(GUID guid );

    @Delete("DELETE FROM `hydra_uofs_files_cluster_mapping` WHERE `file_guid` = #{fileGuid} AND `seg_id` = #{segId}")
    void removeClusterByFileWithId(GUID fileGuid, long segId );

    @Select("SELECT COUNT(*) FROM `hydra_uofs_files_cluster_mapping` WHERE file_guid = #{fileGuid}")
    long countFileClusters( @Param("fileGuid") GUID fileGuid );

    default RemoteCluster getClusterByFileWithId( GUID fileGuid, long segId ){
        GenericRemoteCluster cluster = this.getClusterByFileWithId0(fileGuid, segId);
        if( cluster == null ) {
            return null;
        }

        cluster.setRemoteClusterManipulator( this );
        return cluster;
    }

    @Select("SELECT `id` AS emunId, `file_guid` AS fileGuid, `seg_guid` AS segGuid, `device_guid` AS deviceGuid, `seg_id` AS segId, `crc32`, `size` FROM `hydra_uofs_files_cluster_mapping` WHERE `file_guid` = #{fileGuid} AND `seg_id` = #{segId}")
    GenericRemoteCluster getClusterByFileWithId0( GUID fileGuid, long segId );

}
