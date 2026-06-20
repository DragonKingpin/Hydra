package com.pinecone.hydra.file.ibatis.journal;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.journal.GenericJournal;
import com.pinecone.hydra.storage.file.journal.Journal;
import com.pinecone.hydra.storage.file.journal.JournalStatus;
import com.pinecone.hydra.storage.file.journal.source.JournalManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface JournalMapper extends JournalManipulator {
    void insert( Journal journal );

    void updateStatus( @Param( "guid" ) GUID guid, @Param( "journalStatus" ) JournalStatus journalStatus, @Param( "errorMessage" ) String errorMessage );

    void commit( GUID guid );

    GenericJournal get( GUID guid );

    List<GenericJournal> listByStatus( JournalStatus journalStatus );

    long countByBucketGuid( @Param( "bucketGuid" ) GUID bucketGuid );

    void deleteByBucketGuid( @Param( "bucketGuid" ) GUID bucketGuid );
}
