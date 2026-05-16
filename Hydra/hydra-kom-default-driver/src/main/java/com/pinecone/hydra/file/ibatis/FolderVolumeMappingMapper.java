package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.source.FolderVolumeMappingManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface FolderVolumeMappingMapper extends FolderVolumeMappingManipulator {
    void insert(@Param("folderGuid") GUID folderGuid, @Param("volumeGuid") GUID volumeGuid );

    void remove( @Param("folderGuid") GUID folderGuid, @Param("volumeGuid") GUID volumeGuid );

    GUID getVolumeGuid( GUID folderGuid );
}
