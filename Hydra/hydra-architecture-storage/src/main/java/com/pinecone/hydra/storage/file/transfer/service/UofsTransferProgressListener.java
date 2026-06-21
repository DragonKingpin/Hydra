package com.pinecone.hydra.storage.file.transfer.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.transfer.UofsTransferProgress;

public interface UofsTransferProgressListener extends Pinenut {
    void onProgress( UofsTransferProgress progress );
}
