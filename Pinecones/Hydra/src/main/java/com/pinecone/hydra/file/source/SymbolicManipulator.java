package com.pinecone.hydra.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.file.entity.RemoteFrame;
import com.pinecone.hydra.file.entity.Symbolic;

public interface SymbolicManipulator extends Pinenut {
    Symbolic getSymbolic(GUID guid, ElementNode element);
    void insert( Symbolic symbolic );
    void remove( GUID guid );
    Symbolic getSymbolicByGuid(GUID guid);
}
