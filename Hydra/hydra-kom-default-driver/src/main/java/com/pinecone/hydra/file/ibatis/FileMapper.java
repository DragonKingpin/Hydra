package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.file.source.FileManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface FileMapper extends FileManipulator {
    FileNode getFileNode(GUID guid, ElementNode element);
    void insert( FileNode fileNode );
    void remove( GUID guid );
    GenericFileNode getFileNodeByGuid(GUID guid);

    List<GUID > getGuidsByName(String name );

    List<GUID > getGuidsByNameID(@Param("name") String name, @Param("guid") GUID guid );

    List<GUID > dumpGuid();

    @Override
    long countByBucketGuid( @Param("bucketGuid") GUID bucketGuid );

    @Override
    List<GUID> listGuidsByBucketGuid(
            @Param("bucketGuid") GUID bucketGuid,
            @Param("lastGuid") GUID lastGuid,
            @Param("limit") int limit
    );

    @Override
    void deleteByBucketGuid( @Param("bucketGuid") GUID bucketGuid );

    void update( FileNode fileNode );

    void rename( @Param("guid") GUID guid, @Param("newName") String newName );
}
