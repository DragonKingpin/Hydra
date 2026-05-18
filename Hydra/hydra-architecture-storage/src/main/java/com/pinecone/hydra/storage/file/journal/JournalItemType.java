package com.pinecone.hydra.storage.file.journal;

public enum JournalItemType {
    FILE_NODE,
    CHUNK,
    LOCATION,
    OBJECT,
    VOLUME_EXTENT,
    CLEANUP
}
