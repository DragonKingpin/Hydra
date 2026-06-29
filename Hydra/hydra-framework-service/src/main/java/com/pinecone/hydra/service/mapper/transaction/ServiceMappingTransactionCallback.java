package com.pinecone.hydra.service.mapper.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceMappingTransactionCallback<T> extends Pinenut {
    T execute( ServiceMappingTransactionScope scope ) throws Exception;
}
