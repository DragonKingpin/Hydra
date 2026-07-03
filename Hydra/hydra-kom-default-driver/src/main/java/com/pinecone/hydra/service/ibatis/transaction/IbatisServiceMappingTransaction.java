package com.pinecone.hydra.service.ibatis.transaction;

import com.pinecone.hydra.service.mapper.transaction.ServiceMappingTransaction;
import com.pinecone.hydra.service.mapper.transaction.ServiceMappingTransactionCallback;
import com.pinecone.hydra.service.mapper.transaction.ServiceMappingTransactionScope;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransactionScope;

public class IbatisServiceMappingTransaction implements ServiceMappingTransaction {
    protected IbatisClient mIbatisClient;

    public IbatisServiceMappingTransaction( IbatisClient ibatisClient ) {
        this.mIbatisClient = ibatisClient;
    }

    @Override
    public <T> T required( ServiceMappingTransactionCallback<T> callback ) {
        if ( callback == null ) {
            throw new IllegalArgumentException( "Service mapping transaction callback is null." );
        }

        return this.mIbatisClient.transaction().required( scope -> callback.execute( new Scope( scope ) ) );
    }

    protected static class Scope implements ServiceMappingTransactionScope {
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
