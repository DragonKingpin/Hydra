package com.pinecone.slime.jelly.source.ibatis;

import com.pinecone.framework.system.construction.InstancePool;
import org.apache.ibatis.session.SqlSession;

public class AppliedMapperPool implements InstancePool {
    protected SqlSession        mSqlSession;
    protected Class<? >         mType;

    public AppliedMapperPool ( SqlSession sqlSession, Class<? > type ) {
        this.mSqlSession = sqlSession;
        this.mType       = type;
    }

    @Override
    public Object allocate() {
        return this.mSqlSession.getMapper( this.mType );
    }

    @Override
    public void free( Object obj ) {

    }

    @Override
    public int freeSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int pooledSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void preAllocate( int count ) {
    }

    @Override
    public void setCapacity( int capacity ) {
    }

    @Override
    public int getCapacity() {
        return 0;
    }
}
