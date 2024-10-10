package com.pinecone.hydra.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.file.entity.FileNode;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface FileManipulator extends GUIDNameManipulator {
    FileNode getFileNode(GUID guid, ElementNode element);
    void insert( FileNode fileNode );
    void remove( GUID guid );
    FileNode getFileNodeByGuid(GUID guid);

    List<GUID > getGuidsByName(String name );

    List<GUID > getGuidsByNameID( String name, GUID guid );

    List<GUID > dumpGuid();

}
