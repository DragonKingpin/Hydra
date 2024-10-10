package com.pinecone.hydra.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.file.entity.FileMeta;
import com.pinecone.hydra.file.entity.Folder;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface FolderManipulator extends GUIDNameManipulator {
    Folder getFolder(GUID guid, ElementNode element);
    void insert( Folder folder );
    void remove( GUID guid );
    Folder getFolderByGuid(GUID guid);
    List<GUID > getGuidsByName(String name );

    List<GUID > getGuidsByNameID( String name, GUID guid );
    List<GUID > dumpGuid();

    boolean isFolder(GUID guid);
}
