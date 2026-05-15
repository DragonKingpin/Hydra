package com.pinecone.hydra.storage.file.journal;

public enum JournalStatus {
    PREPARED,
    WRITING,
    COMMITTING,
    COMMITTED,
    ABORTED,
    FAILED
}
