package com.walnut.odin.mapper.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface OdinMappingTransactionScope extends Pinenut {
    <T> T mapper( Class<T> mapperType );
}
