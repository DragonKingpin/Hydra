package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface JournalInstrument extends Pinenut {
    Journal begin( JournalType type, GUID fileGuid, String path );
    void markWriting( GUID journalGuid );
    void markCommitting( GUID journalGuid );
    void commit( GUID journalGuid );
    void abort( GUID journalGuid, String errorMessage );
    void fail( GUID journalGuid, String errorMessage );
    JournalItem addItem( GUID journalGuid, JournalItemType itemType, GUID targetGuid, int ordinalNo );
    JournalItem addItem( GUID journalGuid, JournalItem item );
    void markItemApplied( GUID itemGuid );
    void markItemRolledBack( GUID itemGuid );
    List<Journal> listRecoverable();
}
