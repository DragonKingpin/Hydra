package com.pinecone.hydra.version.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.version.entity.Version;
import com.pinecone.hydra.storage.version.source.VersionManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface VersionMapper extends VersionManipulator {
    @Insert("INSERT INTO `hydra_uofs_version` ( `version_guid`, `version`, `target_storage_object_guid`, `file_guid`) VALUES (#{version}, #{targetStorageObjectGuid}, #{fileGuid})")
    void insertObjectVersion(Version version);

    @Delete("DELETE FROM `hydra_uofs_version` WHERE `version` = #{version} AND `file_guid` = #{fileGuid}")
    void removeObjectVersion( String version, GUID fileGuid );

    @Select("SELECT `target_storage_object_guid` FROM `hydra_uofs_version` WHERE `version` = #{version} AND file_guid = #{fileGuid}")
    GUID queryObjectGuid( String version, GUID fileGuid );


    @Select("SELECT EXISTS(SELECT 1 FROM `hydra_uofs_version` WHERE `file_guid` = #{fileGuid})")
    boolean queryIsManage(@Param("fileGuid") GUID fileGuid);

    @Select("SELECT `target_storage_object_guid` FROM `hydra_uofs_version` WHERE `file_guid` = #{fileGuid}")
    List<GUID> fetchVersions(GUID guid);
}
