package com.pinecone.hydra.storage.file.transfer;

public enum UofsTransferPhase {
    PREPARE,
    SCANNING,
    CREATING_METADATA,
    COPYING_DATA,
    MOVING_METADATA,
    VERIFYING,
    CLEANING_SOURCE,
    DONE,
    FAILED,
    CANCELED
}
