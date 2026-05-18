package com.pinecone.hydra.storage.file.journal.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.journal.GenericJournalItem;
import com.pinecone.hydra.storage.file.journal.JournalItem;
import com.pinecone.hydra.storage.file.journal.JournalItemStatus;
import com.pinecone.hydra.storage.file.journal.JournalItemType;

import java.util.List;

public interface JournalItemManipulator extends Pinenut {
    void insert( JournalItem item );
    void updateStatus( GUID guid, JournalItemStatus itemStatus );
    GenericJournalItem get( GUID guid );
    List<GenericJournalItem> listByJournalGuid( GUID journalGuid );
    List<GenericJournalItem> listByJournalGuidAndStatus( GUID journalGuid, JournalItemStatus itemStatus );
    List<GenericJournalItem> listByJournalGuidAndType( GUID journalGuid, JournalItemType itemType );
}
