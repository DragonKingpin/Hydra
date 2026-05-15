package com.pinecone.hydra.file.ibatis.fat;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.entity.GenericFileChunkLocation;
import com.pinecone.hydra.storage.file.fat.source.FileChunkLocationManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface FileChunkLocationMapper extends FileChunkLocationManipulator {
    @Insert( "INSERT INTO `hydra_uofs_fat_chunk_location` ( `guid`, `chunk_guid`, `replica_no`, `volume_guid`, `location_type`, `object_key`, `object_offset`, `volume_offset`, `length_bytes`, `storage_key`, `version`, `ext_config` ) VALUES ( #{guid}, #{chunkGuid}, #{replicaNo}, #{volumeGuid}, #{locationType}, #{objectKey}, #{objectOffset}, #{volumeOffset}, #{lengthBytes}, #{storageKey}, #{version}, #{extConfig} )" )
    void insert( FileChunkLocation location );

    @Update( "UPDATE `hydra_uofs_fat_chunk_location` SET `volume_guid` = #{volumeGuid}, `location_type` = #{locationType}, `object_key` = #{objectKey}, `object_offset` = #{objectOffset}, `volume_offset` = #{volumeOffset}, `length_bytes` = #{lengthBytes}, `storage_key` = #{storageKey}, `version` = #{version}, `ext_config` = #{extConfig} WHERE `guid` = #{guid}" )
    void update( FileChunkLocation location );

    @Delete( "DELETE FROM `hydra_uofs_fat_chunk_location` WHERE `guid` = #{guid}" )
    void remove( GUID guid );

    @Delete( "DELETE FROM `hydra_uofs_fat_chunk_location` WHERE `chunk_guid` = #{chunkGuid}" )
    void removeByChunkGuid( GUID chunkGuid );

    @Delete( "DELETE FROM `hydra_uofs_fat_chunk_location` WHERE `chunk_guid` IN ( SELECT `guid` FROM `hydra_uofs_fat_chunk` WHERE `file_guid` = #{fileGuid} )" )
    void removeByFileGuid( GUID fileGuid );

    @Select( "SELECT `id`, `guid`, `chunk_guid` AS chunkGuid, `replica_no` AS replicaNo, `volume_guid` AS volumeGuid, `location_type` AS locationType, `object_key` AS objectKey, `object_offset` AS objectOffset, `volume_offset` AS volumeOffset, `length_bytes` AS lengthBytes, `storage_key` AS storageKey, `version`, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_fat_chunk_location` WHERE `guid` = #{guid}" )
    GenericFileChunkLocation get( GUID guid );

    @Select( "SELECT `id`, `guid`, `chunk_guid` AS chunkGuid, `replica_no` AS replicaNo, `volume_guid` AS volumeGuid, `location_type` AS locationType, `object_key` AS objectKey, `object_offset` AS objectOffset, `volume_offset` AS volumeOffset, `length_bytes` AS lengthBytes, `storage_key` AS storageKey, `version`, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_fat_chunk_location` WHERE `chunk_guid` = #{chunkGuid} ORDER BY `replica_no` ASC LIMIT 1" )
    GenericFileChunkLocation getReadyLocation( GUID chunkGuid );

    @Select( "SELECT `id`, `guid`, `chunk_guid` AS chunkGuid, `replica_no` AS replicaNo, `volume_guid` AS volumeGuid, `location_type` AS locationType, `object_key` AS objectKey, `object_offset` AS objectOffset, `volume_offset` AS volumeOffset, `length_bytes` AS lengthBytes, `storage_key` AS storageKey, `version`, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_fat_chunk_location` WHERE `chunk_guid` = #{chunkGuid} ORDER BY `replica_no` ASC" )
    @ResultType( GenericFileChunkLocation.class )
    List<GenericFileChunkLocation> listByChunkGuid( GUID chunkGuid );

    @Select( "SELECT MAX( `volume_offset` + `length_bytes` ) FROM `hydra_uofs_fat_chunk_location` WHERE `volume_guid` = #{volumeGuid} AND `location_type` = 'VOLUME_BLOCK_EXTENT'" )
    Long getMaxEndOffsetByVolumeGuid( GUID volumeGuid );
}
