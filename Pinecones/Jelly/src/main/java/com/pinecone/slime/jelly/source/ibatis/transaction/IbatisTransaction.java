package com.pinecone.slime.jelly.source.ibatis.transaction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface IbatisTransaction extends Pinenut {
    <T> T required( IbatisTransactionCallback<T> callback );
}
