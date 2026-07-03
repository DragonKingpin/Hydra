package com.walnut.odin.task.mapper.transaction;

import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransactionScope;
import com.walnut.odin.mapper.transaction.OdinMappingTransaction;
import com.walnut.odin.mapper.transaction.OdinMappingTransactionCallback;
import com.walnut.odin.mapper.transaction.OdinMappingTransactionScope;

public class IbatisOdinMappingTransaction implements OdinMappingTransaction {
    protected IbatisClient mIbatisClient;

    public IbatisOdinMappingTransaction( IbatisClient ibatisClient ) {
        this.mIbatisClient = ibatisClient;
    }

    @Override
    public <T> T required( OdinMappingTransactionCallback<T> callback ) {
        if ( callback == null ) {
            throw new IllegalArgumentException( "Odin mapping transaction callback is null." );
        }

        return this.mIbatisClient.transaction().required( scope -> callback.execute( new Scope( scope ) ) );
    }

    protected static class Scope implements OdinMappingTransactionScope {
        protected IbatisTransactionScope mIbatisScope;

        public Scope( IbatisTransactionScope ibatisScope ) {
            this.mIbatisScope = ibatisScope;
        }

        @Override
        public <T> T mapper( Class<T> mapperType ) {
            return this.mIbatisScope.mapper( mapperType );
        }
    }
}
