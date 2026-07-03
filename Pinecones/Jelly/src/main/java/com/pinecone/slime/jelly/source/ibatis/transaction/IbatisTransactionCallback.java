package com.pinecone.slime.jelly.source.ibatis.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface IbatisTransactionCallback<T> extends Pinenut {
    T execute( IbatisTransactionScope scope ) throws Exception;
}
