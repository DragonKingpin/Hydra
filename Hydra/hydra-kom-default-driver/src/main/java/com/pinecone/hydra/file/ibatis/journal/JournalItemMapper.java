package com.pinecone.hydra.file.ibatis.journal;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.journal.GenericJournalItem;
import com.pinecone.hydra.storage.file.journal.JournalItem;
import com.pinecone.hydra.storage.file.journal.JournalItemStatus;
import com.pinecone.hydra.storage.file.journal.JournalItemType;
import com.pinecone.hydra.storage.file.journal.source.JournalItemManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface JournalItemMapper extends JournalItemManipulator {
    void insert( JournalItem item );

    void updateStatus( @Param( "guid" ) GUID guid, @Param( "itemStatus" ) JournalItemStatus itemStatus );

    GenericJournalItem get( GUID guid );

    List<GenericJournalItem> listByJournalGuid( GUID journalGuid );

    List<GenericJournalItem> listByJournalGuidAndStatus( @Param( "journalGuid" ) GUID journalGuid, @Param( "itemStatus" ) JournalItemStatus itemStatus );

    List<GenericJournalItem> listByJournalGuidAndType( @Param( "journalGuid" ) GUID journalGuid, @Param( "itemType" ) JournalItemType itemType );
}
