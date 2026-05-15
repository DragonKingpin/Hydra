package com.pinecone.hydra.file.ibatis.fat;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.GenericFileChunk;
import com.pinecone.hydra.storage.file.fat.source.FileChunkManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface FileChunkMapper extends FileChunkManipulator {
    @Insert( "INSERT INTO `hydra_uofs_fat_chunk` ( `guid`, `file_guid`, `chunk_index`, `logical_offset`, `chunk_size`, `valid_size`, `crc32`, `checksum`, `ext_config` ) VALUES ( #{guid}, #{fileGuid}, #{chunkIndex}, #{logicalOffset}, #{chunkSize}, #{validSize}, #{crc32}, #{checksum}, #{extConfig} )" )
    void insert( FileChunk chunk );

    @Update( "UPDATE `hydra_uofs_fat_chunk` SET `chunk_size` = #{chunkSize}, `valid_size` = #{validSize}, `crc32` = #{crc32}, `checksum` = #{checksum}, `ext_config` = #{extConfig} WHERE `guid` = #{guid}" )
    void update( FileChunk chunk );

    @Delete( "DELETE FROM `hydra_uofs_fat_chunk` WHERE `guid` = #{guid}" )
    void remove( GUID guid );

    @Delete( "DELETE FROM `hydra_uofs_fat_chunk` WHERE `file_guid` = #{fileGuid}" )
    void removeByFileGuid( GUID fileGuid );

    @Select( "SELECT `id`, `guid`, `file_guid` AS fileGuid, `chunk_index` AS chunkIndex, `logical_offset` AS logicalOffset, `chunk_size` AS chunkSize, `valid_size` AS validSize, `crc32`, `checksum`, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_fat_chunk` WHERE `guid` = #{guid}" )
    GenericFileChunk get( GUID guid );

    @Select( "SELECT `id`, `guid`, `file_guid` AS fileGuid, `chunk_index` AS chunkIndex, `logical_offset` AS logicalOffset, `chunk_size` AS chunkSize, `valid_size` AS validSize, `crc32`, `checksum`, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_fat_chunk` WHERE `file_guid` = #{fileGuid} AND `chunk_index` = #{chunkIndex}" )
    GenericFileChunk getByFileGuidAndIndex( @Param( "fileGuid" ) GUID fileGuid, @Param( "chunkIndex" ) long chunkIndex );

    @Select( "SELECT `id`, `guid`, `file_guid` AS fileGuid, `chunk_index` AS chunkIndex, `logical_offset` AS logicalOffset, `chunk_size` AS chunkSize, `valid_size` AS validSize, `crc32`, `checksum`, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_fat_chunk` WHERE `file_guid` = #{fileGuid} ORDER BY `chunk_index` ASC" )
    @ResultType( GenericFileChunk.class )
    List<GenericFileChunk> listByFileGuid( GUID fileGuid );

    @Select( "SELECT `id`, `guid`, `file_guid` AS fileGuid, `chunk_index` AS chunkIndex, `logical_offset` AS logicalOffset, `chunk_size` AS chunkSize, `valid_size` AS validSize, `crc32`, `checksum`, `ext_config` AS extConfig, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_uofs_fat_chunk` WHERE `file_guid` = #{fileGuid} AND `logical_offset` < #{endOffset} AND `logical_offset` + `valid_size` > #{startOffset} ORDER BY `chunk_index` ASC" )
    @ResultType( GenericFileChunk.class )
    List<GenericFileChunk> listByFileRange( @Param( "fileGuid" ) GUID fileGuid, @Param( "startOffset" ) long startOffset, @Param( "endOffset" ) long endOffset );
}
