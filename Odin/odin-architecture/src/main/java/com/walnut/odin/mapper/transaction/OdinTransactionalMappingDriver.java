package com.walnut.odin.mapper.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface OdinTransactionalMappingDriver extends Pinenut {
    OdinMappingTransaction transaction();
}
