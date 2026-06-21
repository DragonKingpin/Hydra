package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.GenericSymbolic;
import com.pinecone.hydra.storage.file.entity.Symbolic;
import com.pinecone.hydra.storage.file.source.SymbolicManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface SymbolicMapper extends SymbolicManipulator {
    @Override
    default Symbolic getSymbolic( GUID guid, ElementNode element ) {
        return this.getSymbolicByGuid( guid );
    }

    void insert( Symbolic symbolic );

    void remove( GUID guid );

    void rename( @Param("guid") GUID guid, @Param("newName") String newName );

    GenericSymbolic getSymbolicByGuid( GUID guid );

    GenericSymbolic getSymbolicByNameGuid( @Param("nodeName") String nodeName, @Param("guid") GUID guid );

    boolean isSymbolicMatchedByNameGuid( @Param("nodeName") String nodeName, @Param("guid") GUID guid );

    @Override
    List<GenericSymbolic> listPage(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Override
    long count( @Param("keyword") String keyword );

    @Override
    long countByBucketGuid( @Param("bucketGuid") GUID bucketGuid );

    @Override
    void deleteByBucketGuid( @Param("bucketGuid") GUID bucketGuid );

    List<GUID> getGuidsByName( String name );

    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
