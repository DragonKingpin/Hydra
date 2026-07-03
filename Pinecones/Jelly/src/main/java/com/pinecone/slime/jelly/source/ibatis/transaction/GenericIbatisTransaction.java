package com.pinecone.slime.jelly.source.ibatis.transaction;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import org.apache.ibatis.session.SqlSession;

public class GenericIbatisTransaction implements IbatisTransaction {

    protected static final ThreadLocal<Scope> CURRENT_SCOPE = new ThreadLocal<>();

    protected IbatisClient mClient;

    public GenericIbatisTransaction( IbatisClient client ) {
        this.mClient = client;
    }

    @Override
    public <T> T required( IbatisTransactionCallback<T> callback ) {
        if ( callback == null ) {
            throw new IllegalArgumentException( "Ibatis transaction callback is null." );
        }

        Scope scope = CURRENT_SCOPE.get();
        if ( scope != null ) {
            if ( scope.client() != this.mClient ) {
                throw new IllegalStateException( "Nested Ibatis transaction across different clients is not supported." );
            }
            return this.executeCallback( callback, scope );
        }

        SqlSession sqlSession = this.mClient.openSession( false );
        Scope createdScope = new Scope( this.mClient, sqlSession );
        CURRENT_SCOPE.set( createdScope );
        try {
            T result = this.executeCallback( callback, createdScope );
            sqlSession.commit();
            return result;
        }
        catch ( RuntimeException e ) {
            this.rollback( sqlSession );
            throw e;
        }
        catch ( Error e ) {
            this.rollback( sqlSession );
            throw e;
        }
        finally {
            CURRENT_SCOPE.remove();
            sqlSession.close();
        }
    }

    protected <T> T executeCallback( IbatisTransactionCallback<T> callback, Scope scope ) {
        try {
            return callback.execute( scope );
        }
        catch ( RuntimeException e ) {
            throw e;
        }
        catch ( Error e ) {
            throw e;
        }
        catch ( Exception e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }

    protected void rollback( SqlSession sqlSession ) {
        try {
            sqlSession.rollback();
        }
        catch ( RuntimeException ignore ) {
            // Preserve the original failure.
        }
    }

    protected static class Scope implements IbatisTransactionScope {
        protected IbatisClient mClient;
        protected SqlSession   mSqlSession;

        public Scope( IbatisClient client, SqlSession sqlSession ) {
            this.mClient = client;
            this.mSqlSession = sqlSession;
        }

        public IbatisClient client() {
            return this.mClient;
        }

        @Override
        public <T> T mapper( Class<T> mapperType ) {
            return this.mSqlSession.getMapper( mapperType );
        }

        @Override
        public SqlSession sqlSession() {
            return this.mSqlSession;
        }
    }
}
