package com.pinecone.hydra.storage.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.Symbolic;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface SymbolicManipulator extends Pinenut, GUIDNameManipulator {
    Symbolic getSymbolic(GUID guid, ElementNode element);
    void insert( Symbolic symbolic );
    void remove( GUID guid );
    Symbolic getSymbolicByGuid(GUID guid);
    Symbolic getSymbolicByNameGuid( String nodeName, GUID guid );
    boolean isSymbolicMatchedByNameGuid( String nodeName, GUID guid );
    List<? extends Symbolic> listPage( String keyword, int offset, int limit );
    long count( String keyword );
}
