package com.walnut.sparta.ucdn.console.mapper;

import com.pinecone.framework.util.id.GUID;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ClusterFileSyncMapper {
    @Insert("INSERT INTO `hydra_ucdn_fmd_sync_status` (`file_guid`, `state`, `site_guid`) VALUES ( #{fileGuid},#{state},#{siteGuid})")
    void insert(@Param("fileGuid") GUID fileGuid, @Param("state") int state, @Param("siteGuid") GUID siteGuid);

    @Delete("DELETE FROM `hydra_ucdn_fmd_sync_status` WHERE `file_guid` = #{fileGuid}")
    void remove( GUID fileGuid );

    @Update("UPDATE `hydra_ucdn_fmd_sync_status` SET `state` = #{state} WHERE `file_guid` = #{fileGuid}")
    void updateState(@Param("fileGuid") GUID fileGuid, @Param("state") int state );

    @Select("SELECT `state` FROM `hydra_ucdn_fmd_sync_status` WHERE `file_guid` = #{fileGuid}")
    Integer queryState( @Param("fileGuid") GUID fileGuid );
}
