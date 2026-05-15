package com.pinecone.hydra.storage.file.journal;

public enum JournalItemStatus {
    PREPARED,
    APPLIED,
    ROLLED_BACK,
    FAILED
}
