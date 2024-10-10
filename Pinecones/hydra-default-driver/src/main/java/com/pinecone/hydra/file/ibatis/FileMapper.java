package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.file.entity.FileNode;
import com.pinecone.hydra.file.entity.GenericFileNode;
import com.pinecone.hydra.file.source.FileManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface FileMapper extends FileManipulator {
    FileNode getFileNode(GUID guid, ElementNode element);
    @Insert("INSERT INTO `hydra_uofs_files` (`guid`, `create_time`, `update_time`, `deleted_at`, `name`, `checksum`, `parity_check`, `size`) VALUES (#{guid},#{createTime},#{updateTime},#{deletedTime},#{name},#{checksum},#{parityCheck},#{size})")
    void insert( FileNode fileNode );
    @Delete("DELETE FROM `hydra_uofs_files` WHERE `guid` = #{guid}")
    void remove( GUID guid );
    @Select("SELECT `id` AS enumId, `guid`, `create_time` AS createTime, `update_time` AS updateTime, `deleted_at` AS deletedTime, `name`, `checksum`, `parity_check` AS parityCheck, `size` FROM hydra_uofs_files WHERE `guid` = #{guid}")
    GenericFileNode getFileNodeByGuid(GUID guid);

    @Select("SELECT `guid` FROM `hydra_uofs_files` WHERE `name` = #{name}")
    List<GUID > getGuidsByName(String name );

    @Select("SELECT `guid` FROM `hydra_uofs_files` WHERE `name` = #{name} AND `guid` = #{guid}")
    List<GUID > getGuidsByNameID(@Param("name") String name, @Param("guid") GUID guid );

    @Select("SELECT `guid` FROM hydra_uofs_files ")
    List<GUID > dumpGuid();
}
