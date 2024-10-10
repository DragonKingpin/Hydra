package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.file.entity.Symbolic;
import com.pinecone.hydra.file.source.SymbolicManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@IbatisDataAccessObject
public interface SymbolicMapper extends SymbolicManipulator {
    Symbolic getSymbolic(GUID guid, ElementNode element);
    void insert( Symbolic symbolic );
    void remove( GUID guid );
    Symbolic getSymbolicByGuid(GUID guid);
}
