package com.pinecone.hydra.storage.file;

public final class FileConstants {
    public static final Number DefaultChunkSize = 10 * 1024 * 1024L; // 10 MB
    public static final Number TinyFileStripSizing = 10 * 1024 * 1024L; // 10 MB
    public static final boolean DefaultJournalEnabled = true;
    public static final boolean DefaultJournalAutoRecoveryEnabled = true;

    public static final String StorageVersionSignature = "Generic";

}
