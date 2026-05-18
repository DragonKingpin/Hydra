package com.pinecone.hydra.storage.file.journal.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.journal.GenericJournal;
import com.pinecone.hydra.storage.file.journal.Journal;
import com.pinecone.hydra.storage.file.journal.JournalStatus;

import java.util.List;

public interface JournalManipulator extends Pinenut {
    void insert( Journal journal );
    void updateStatus( GUID guid, JournalStatus journalStatus, String errorMessage );
    void commit( GUID guid );
    GenericJournal get( GUID guid );
    List<GenericJournal> listByStatus( JournalStatus journalStatus );
}
