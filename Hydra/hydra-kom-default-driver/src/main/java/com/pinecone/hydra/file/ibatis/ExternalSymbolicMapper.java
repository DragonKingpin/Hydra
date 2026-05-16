package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.storage.file.entity.GenericExternalSymbolic;
import com.pinecone.hydra.storage.file.source.ExternalSymbolicManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@IbatisDataAccessObject
public interface ExternalSymbolicMapper extends ExternalSymbolicManipulator {
    void insert( ExternalSymbolic externalSymbolic );
    void remove( GUID guid );

    GenericExternalSymbolic getSymbolicByGuid( GUID guid );

    GenericExternalSymbolic getSymbolicByNameGuid(String nodeName, GUID guid );

    boolean isSymbolicMatchedByNameGuid(String nodeName, GUID guid );
}
