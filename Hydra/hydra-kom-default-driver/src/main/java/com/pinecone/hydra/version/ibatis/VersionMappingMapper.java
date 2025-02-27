package com.pinecone.hydra.version.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.version.entity.TitanVersionMapping;
import com.pinecone.hydra.storage.version.entity.VersionMapping;
import com.pinecone.hydra.storage.version.source.VersionMappingManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface VersionMappingMapper extends VersionMappingManipulator {
@Insert("INSERT INTO `hydra_ucdn_version_mapping` ( `enable_version_guid`, `file_guid`,`version_guid`) VALUES (#{enableVersionGuid},#{fileGuid},#{versionGuid})")
    void insert(VersionMapping versionMapping);
@Delete("DELETE FROM `hydra_ucdn_version_mapping` WHERE `enable_version_guid` = #{enableVersionGuid} AND `file_guid` = #{fileGuid} AND `version_guid` = #{versionGuid}")
    void remove(VersionMapping versionMapping);
@Select("SELECT `enable_version_guid` AS enableVersionGuid, `file_guid` AS fileGuid, `version_guid` AS versionGuid FROM `hydra_ucdn_version_mapping` WHERE `file_guid` = #{fileGuid}")
    TitanVersionMapping queryVersionMapping(GUID fileGuid);
    @Insert("UPDATE `hydra_ucdn_version_mapping` "
            + "SET `enable_version_guid` = #{enableVersionGuid}, "
            + "`version_guid` = #{versionGuid} "
            + "WHERE `file_guid` = #{fileGuid}")
    void update(VersionMapping versionMapping);

    @Select("SELECT `enable_version_guid` AS enableVersionGuid, `file_guid` AS fileGuid, `version_guid` AS versionGuid FROM `hydra_ucdn_version_mapping`")
    List<TitanVersionMapping> queryAllVersionMapper();
    @Select("SELECT EXISTS(SELECT 1 FROM `hydra_ucdn_version_mapping` WHERE `enable_version_guid` = #{enableVersionGuid})")
    boolean isExistEnableVersionMapping(GUID enableVersionGuid);
}
