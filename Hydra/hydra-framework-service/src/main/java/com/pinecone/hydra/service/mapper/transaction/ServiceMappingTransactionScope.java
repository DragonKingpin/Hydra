package com.pinecone.hydra.service.mapper.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceMappingTransactionScope extends Pinenut {
    <T> T mapper( Class<T> mapperType );
}
