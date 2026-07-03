package com.pinecone.hydra.storage.file.transfer.service;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.transfer.UofsTransferRequest;
import com.pinecone.hydra.storage.file.transfer.UofsTransferTask;

public interface UofsTransferExecutor extends Pinenut {
    UofsTransferTask execute( UofsTransferRequest request );

    UofsTransferTask execute( UofsTransferRequest request, @Nullable UofsTransferProgressListener progressListener );
}
