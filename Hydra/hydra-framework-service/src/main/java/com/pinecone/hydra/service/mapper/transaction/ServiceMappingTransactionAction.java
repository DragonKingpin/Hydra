package com.pinecone.hydra.service.mapper.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceMappingTransactionAction extends Pinenut {
    void execute( ServiceMappingTransactionScope scope );
}
