package com.walnut.odin.mapper.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface OdinMappingTransactionAction extends Pinenut {
    void execute( OdinMappingTransactionScope scope ) throws Exception;
}
