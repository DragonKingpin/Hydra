package com.pinecone.hydra.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.file.entity.Symbolic;
import com.pinecone.hydra.file.entity.SymbolicMeta;

public interface SymbolicMetaManipulator extends Pinenut {
    SymbolicMeta getSymbolicMeta(GUID guid, ElementNode element);
    void insert( SymbolicMeta symbolicMeta );
    void remove( GUID guid );
    SymbolicMeta getSymbolicMetaByGuid(GUID guid);
}
