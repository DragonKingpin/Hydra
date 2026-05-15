package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.system.prototype.Pinenut;

public interface JournalRecoveryInstrument extends Pinenut {
    void recover();
}
