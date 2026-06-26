package com.walnut.odin.mapper.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface OdinMappingTransactionCallback<T> extends Pinenut {
    T execute( OdinMappingTransactionScope scope ) throws Exception;
}
