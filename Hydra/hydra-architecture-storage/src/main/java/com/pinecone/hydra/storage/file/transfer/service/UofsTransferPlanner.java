package com.pinecone.hydra.storage.file.transfer.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.transfer.UofsTransferPlan;
import com.pinecone.hydra.storage.file.transfer.UofsTransferRequest;

public interface UofsTransferPlanner extends Pinenut {
    UofsTransferPlan plan( UofsTransferRequest request );
}
