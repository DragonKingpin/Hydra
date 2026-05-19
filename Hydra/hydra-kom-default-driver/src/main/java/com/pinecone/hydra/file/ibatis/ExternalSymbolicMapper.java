package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.storage.file.entity.GenericExternalSymbolic;
import com.pinecone.hydra.storage.file.source.ExternalSymbolicManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ExternalSymbolicMapper extends ExternalSymbolicManipulator {
    void insert( ExternalSymbolic externalSymbolic );
    void remove( GUID guid );

    GenericExternalSymbolic getSymbolicByGuid( GUID guid );

    GenericExternalSymbolic getSymbolicByNameGuid( @Param("nodeName") String nodeName, @Param("guid") GUID guid );

    boolean isSymbolicMatchedByNameGuid( @Param("nodeName") String nodeName, @Param("guid") GUID guid );

    @Override
    List<GenericExternalSymbolic> listPage(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Override
    long count( @Param("keyword") String keyword );
}
