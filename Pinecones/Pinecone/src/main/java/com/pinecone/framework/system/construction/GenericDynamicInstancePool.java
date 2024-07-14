package com.pinecone.framework.system.construction;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.lang.DynamicFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GenericDynamicInstancePool<T > implements DynamicInstancePool<T > {
    private BlockingQueue<T > mPool;
    private DynamicFactory    mFactory;
    private Class<T >         mClassType;
    private int               mCapacity;
    private int               mFreeSize;
    private int               mPreAllocate;

    public GenericDynamicInstancePool(DynamicFactory factory, Class<T > classType ) {
        this( factory, 0, 0, classType );
    }

    public GenericDynamicInstancePool(DynamicFactory factory, int preAllocate, Class<T > classType ) {
        this( factory, 0, preAllocate, classType );
    }

    public GenericDynamicInstancePool(DynamicFactory factory, int capacity, int preAllocate, Class<T > classType ) {
        this.mPool        = new LinkedBlockingQueue<>();
        this.mFactory     = factory;
        this.mCapacity    = capacity > 0 ? capacity : Integer.MAX_VALUE;
        this.mClassType   = classType;
        this.mPreAllocate = preAllocate;
        this.mFreeSize    = this.mCapacity;

        this.preAllocate( preAllocate );
    }

    protected T newInstance() {
        try{
            return this.mClassType.cast( this.mFactory.newInstance( this.mClassType, null, null ) );
        }
        catch ( Exception e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }

    @Override
    public T allocate() {
        T obj = this.mPool.poll();
        if ( obj == null ) {
            int availableCapacity = this.freeSize();
            if ( availableCapacity > 0 ) {
                int allocateCount = 1;
                if( this.mPreAllocate > 0 ) {
                    allocateCount = Math.min( availableCapacity, this.mPreAllocate );
                }
                this.preAllocate( allocateCount );
                obj = this.mPool.poll();
                if ( obj == null ) {
                    throw new InternalError( "Unable to allocate instance." );
                }
            }
            else {
                throw new IllegalStateException( "Out of capacity, too many instances[" + this.mCapacity + "]." );
            }
        }

        --this.mFreeSize;
        return obj;
    }

    @Override
    public void free( T obj ) {
        if ( obj != null ) {
            this.mPool.offer( obj );
            ++this.mFreeSize;
        }
    }

    @Override
    public int freeSize() {
        return this.mFreeSize;
    }

    @Override
    public int pooledSize() {
        return this.mPool.size();
    }

    @Override
    public boolean isEmpty() {
        return this.freeSize() == 0;
    }

    @Override
    public void preAllocate( int count ) {
        for ( int i = 0; i < count; ++i) {
            this.mPool.offer( this.newInstance() );
        }
    }

    @Override
    public void setCapacity( int capacity ) {
        if ( capacity < this.mCapacity - this.mFreeSize ) {
            throw new IllegalArgumentException( "New capacity cannot be less than current capacity minus free size." );
        }
        if ( capacity > this.mCapacity ) {
            int availableCapacity = this.freeSize();
            if ( availableCapacity > 0 ) {
                if( this.mPreAllocate > 0 ) {
                    this.preAllocate( Math.min( availableCapacity, this.mPreAllocate ) );
                }
            }
        }
        this.mCapacity = capacity;
    }

    @Override
    public int getCapacity() {
        return this.mCapacity;
    }

}
