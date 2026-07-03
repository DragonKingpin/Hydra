package com.pinecone.hydra.service.mapper.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceTransactionalMappingDriver extends Pinenut {
    ServiceMappingTransaction transaction();
}
