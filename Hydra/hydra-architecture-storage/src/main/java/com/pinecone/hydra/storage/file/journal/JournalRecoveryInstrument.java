package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.system.regime.Instrument;

public interface JournalRecoveryInstrument extends Instrument {

    void recover();

}
