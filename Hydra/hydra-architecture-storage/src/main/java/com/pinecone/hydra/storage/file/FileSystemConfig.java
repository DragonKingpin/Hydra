package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.StorageConfig;
import com.pinecone.hydra.storage.file.reparse.UofsSymbolicResolveConfig;
import com.pinecone.hydra.system.ko.KernelObjectConfig;

public interface FileSystemConfig extends StorageConfig {
    String getVersionSignature();

    Number getChunkSize();

    GUID getLocalhostGUID();

    Number getmTinyFileStripSizing();

    long getPathQueryExpiryTimeHotMil();

    boolean isJournalEnabled();

    boolean isJournalAutoRecoveryEnabled();

    default int getSymbolicReparseMaxDepth() {
        return UofsSymbolicResolveConfig.DefaultMaxDepth;
    }

    default int getRemoveAsyncThreshold() {
        return 32;
    }
}
