package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.entity.GenericFolder;
import com.pinecone.hydra.storage.file.source.FolderManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface FolderMapper extends FolderManipulator {
    Folder getFolder(GUID guid, ElementNode element);
    void insert( Folder folder );
    void remove( GUID guid );
    void update( Folder folder );
    GenericFolder getFolderByGuid(GUID guid);
    List<GUID > getGuidsByName(String name );
    List<GUID > getGuidsByNameID(@Param("name") String name, @Param("guid") GUID guid );

    List<GUID > dumpGuid();
    boolean isFolder(GUID guid);

    void rename( @Param("fileGuid") GUID fileGuid, @Param("newName") String newName );
}
