package com.pinecone.framework.system.construction;

import com.pinecone.framework.system.BadAllocateException;
import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.lang.DynamicFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class GenericDynamicInstancePool<T > implements DynamicInstancePool<T > {
    private final Queue<T >        mPool;
    private final DynamicFactory   mFactory;
    private final Class<T >        mClassType;
    private final AtomicInteger    mCapacity;
    private final AtomicInteger    mCreatedSize;
    private final PoolSemaphore    mPermits;
    private final ReentrantLock    mCreationLock;
    private final ReentrantLock    mStateLock;
    private final int              mPreAllocate;

    public GenericDynamicInstancePool( DynamicFactory factory, Class<T > classType ) {
        this( factory, 0, 0, classType );
    }

    public GenericDynamicInstancePool( DynamicFactory factory, int preAllocate, Class<T > classType ) {
        this( factory, 0, preAllocate, classType );
    }

    public GenericDynamicInstancePool( DynamicFactory factory, int capacity, int preAllocate, Class<T > classType ) {
        int nCapacity     = capacity > 0 ? capacity : Integer.MAX_VALUE;
        this.mPool        = new ConcurrentLinkedQueue<>();
        this.mFactory     = factory;
        this.mCapacity    = new AtomicInteger( nCapacity );
        this.mClassType   = classType;
        this.mPreAllocate = preAllocate;
        this.mCreatedSize = new AtomicInteger( 0 );
        this.mPermits     = new PoolSemaphore( nCapacity );
        this.mCreationLock = new ReentrantLock();
        this.mStateLock    = new ReentrantLock();

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
    public T allocate() throws BadAllocateException {
        if ( !this.mPermits.tryAcquire() ) {
            throw new BadAllocateException( "Out of capacity, too many instances[" + this.mCapacity.get() + "]." );
        }

        T obj = this.mPool.poll();
        if ( obj != null ) {
            return obj;
        }

        return this.allocateNewAfterPermitAcquired();
    }

    protected T allocateNewAfterPermitAcquired() throws BadAllocateException {
        this.mCreationLock.lock();
        try {
            T obj = this.mPool.poll();
            if ( obj != null ) {
                return obj;
            }

            int created = this.mCreatedSize.get();
            int capacity = this.mCapacity.get();
            if ( created >= capacity ) {
                this.mPermits.release();
                throw new BadAllocateException( "Unable to allocate instance." );
            }

            this.mCreatedSize.incrementAndGet();
            try {
                return this.newInstance();
            }
            catch ( RuntimeException e ) {
                this.mCreatedSize.decrementAndGet();
                this.mPermits.release();
                throw new BadAllocateException( e );
            }
        }
        finally {
            this.mCreationLock.unlock();
        }
    }

    @Override
    public void free( T obj ) {
        if ( obj != null ) {
            this.mStateLock.lock();
            try {
                if ( this.mPermits.availablePermits() >= this.mCapacity.get() ) {
                    throw new IllegalStateException( "Instance pool is already full[" + this.mCapacity.get() + "]." );
                }
                this.mPool.offer( obj );
                this.mPermits.release();
            }
            finally {
                this.mStateLock.unlock();
            }
        }
    }

    @Override
    public int freeSize() {
        return this.mPermits.availablePermits();
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
        if ( count <= 0 ) {
            return;
        }

        this.mCreationLock.lock();
        try {
            int nCount = Math.min( count, Math.max( 0, this.mCapacity.get() - this.mCreatedSize.get() ) );
            for ( int i = 0; i < nCount; ++i) {
                this.mPool.offer( this.newInstance() );
                this.mCreatedSize.incrementAndGet();
            }
        }
        finally {
            this.mCreationLock.unlock();
        }
    }

    @Override
    public void setCapacity( int capacity ) {
        int nCapacity = capacity > 0 ? capacity : Integer.MAX_VALUE;
        this.mStateLock.lock();
        this.mCreationLock.lock();
        try {
            int nOldCapacity = this.mCapacity.get();
            int borrowed = nOldCapacity - this.mPermits.availablePermits();
            if ( nCapacity < borrowed ) {
                throw new IllegalArgumentException( "New capacity cannot be less than current capacity minus free size." );
            }

            this.mCapacity.set( nCapacity );
            int delta = nCapacity - nOldCapacity;
            if ( delta > 0 ) {
                this.mPermits.release( delta );
                if( this.mPreAllocate > 0 ) {
                    this.preAllocate( Math.min( this.freeSize(), this.mPreAllocate ) );
                }
            }
            else if ( delta < 0 ) {
                this.mPermits.reduce( -delta );
                int maxIdle = nCapacity - borrowed;
                while ( this.mPool.size() > maxIdle ) {
                    T obj = this.mPool.poll();
                    if ( obj == null ) {
                        break;
                    }
                    this.mCreatedSize.decrementAndGet();
                }
            }
        }
        finally {
            this.mCreationLock.unlock();
            this.mStateLock.unlock();
        }
    }

    @Override
    public int getCapacity() {
        return this.mCapacity.get();
    }

    private static class PoolSemaphore extends Semaphore {
        PoolSemaphore( int permits ) {
            super( permits );
        }

        void reduce( int reduction ) {
            this.reducePermits( reduction );
        }
    }

}
