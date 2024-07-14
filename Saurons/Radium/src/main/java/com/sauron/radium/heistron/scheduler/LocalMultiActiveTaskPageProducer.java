package com.sauron.radium.heistron.scheduler;

import com.sauron.radium.heistron.Heistium;
import com.pinecone.slime.chunk.ContiguousPage;
import com.pinecone.slime.chunk.scheduler.PageDivider;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocalMultiActiveTaskPageProducer extends ActiveTaskPageProducer {
    protected Heistium mHeistium;
    protected ReadWriteLock     mActivateLock;
    protected CountDownLatch    mTaskCountDownLatch;

    public LocalMultiActiveTaskPageProducer( Heistium heistium, PageDivider divider, long autoIncrementId ) {
        super( divider, autoIncrementId );

        this.mHeistium           = heistium;
        this.mActivateLock       = new ReentrantReadWriteLock();

        long nProductsSum        = this.getProductsSum();
        if( nProductsSum > Integer.MAX_VALUE ) {
            throw new IllegalArgumentException( "Number of local tasks should not above INT32_MAX" );
        }
        this.mTaskCountDownLatch = new CountDownLatch( (int)nProductsSum );
    }

    @Override
    public boolean hasTerminateSignal() {
        return this.mHeistium.queryTerminationSignal().get();
    }

    @Override
    public boolean hasMoreProducts() {
        this.mActivateLock.readLock().lock();
        try {
            return super.hasMoreProducts();
        }
        finally {
            this.mActivateLock.readLock().unlock();
        }
    }

    @Override
    public TaskPage require() {
        return this.activate();
    }

    @Override
    public TaskPage activate() {
        this.mActivateLock.writeLock().lock();
        try {
            return (TaskPage) super.activate();
        }
        finally {
            this.mActivateLock.writeLock().unlock();
        }
    }

    @Override
    public void activate( ContiguousPage that ) {
        this.mActivateLock.writeLock().lock();
        try {
            super.activate( that );
        }
        finally {
            this.mActivateLock.writeLock().unlock();
        }
    }

    @Override
    public void deactivate( ContiguousPage that ) {
        this.mActivateLock.writeLock().lock();
        try {
            super.deactivate( that );
        }
        finally {
            this.mActivateLock.writeLock().unlock();
        }

        this.mTaskCountDownLatch.countDown();
    }

    @Override
    public void deactivate( ContiguousPage[] those ) {
        this.mActivateLock.writeLock().lock();
        try {
            for ( ContiguousPage p : those ) {
                super.deactivate( p );
                this.mTaskCountDownLatch.countDown();
            }
        }
        finally {
            this.mActivateLock.writeLock().unlock();
        }
    }

    @Override
    public long getActivatedSize() {
        this.mActivateLock.readLock().lock();
        try {
            return super.getActivatedSize();
        }
        finally {
            this.mActivateLock.readLock().unlock();
        }
    }

    @Override
    public ContiguousPage getPageById(long id ) {
        this.mActivateLock.readLock().lock();
        try {
            return super.getPageById( id );
        }
        finally {
            this.mActivateLock.readLock().unlock();
        }
    }

    @Override
    public long getProductsSum() {
        return this.getDivider().getMaxAllocations();
    }

    @Override
    public void awaitProducerFinished() throws InterruptedException {
        this.mTaskCountDownLatch.await();
    }

    @Override
    public void awaitProducerFinished( long timeout, TimeUnit unit ) throws InterruptedException {
        this.mTaskCountDownLatch.await( timeout, unit );
    }

    @Override
    public boolean isFinished() {
        return this.mTaskCountDownLatch.getCount() <= 0;
    }
}
