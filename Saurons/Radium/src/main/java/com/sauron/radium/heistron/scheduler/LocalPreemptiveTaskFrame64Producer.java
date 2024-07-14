package com.sauron.radium.heistron.scheduler;

import com.sauron.radium.heistron.Heistium;
import com.pinecone.slime.unitization.MinMaxRange;
import com.pinecone.slime.chunk.RangedPage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocalPreemptiveTaskFrame64Producer implements TaskFrame64Producer {
    protected Heistium         mHeistium;
    protected RangedPage       mMasterPage;
    protected ReadWriteLock    mActivateLock;
    protected CountDownLatch   mTaskCountDownLatch;

    protected long             mnProductStartOffset;
    protected AtomicLong       mFinishedTasks;
    protected long             mnProductsSum;
    protected long             mnFinishedProductsSum;

    public LocalPreemptiveTaskFrame64Producer( Heistium heistium, RangedPage masterPage ) {
        this.mHeistium              = heistium;
        this.mMasterPage            = masterPage;
        this.mActivateLock          = new ReentrantReadWriteLock();

        MinMaxRange range = (MinMaxRange)this.mMasterPage.getRange();
        this.mnProductStartOffset   = range.getMin().longValue();
        this.mnProductsSum          = range.span().longValue();
        this.mnFinishedProductsSum  = this.mnProductsSum + this.mnProductStartOffset;

        if( this.mnProductsSum > Integer.MAX_VALUE ) {
            throw new IllegalArgumentException( "Number of local tasks should not above INT32_MAX" );
        }

        this.mFinishedTasks         = new AtomicLong     ( this.mnProductStartOffset    );
        this.mTaskCountDownLatch    = new CountDownLatch( (int)this.mnProductsSum );
    }

    @Override
    public boolean hasTerminateSignal() {
        return this.mHeistium.queryTerminationSignal().get();
    }

    @Override
    public long getProductsSum() {
        return this.mnProductsSum;
    }

    @Override
    public boolean hasMoreProducts() {
        return this.mFinishedTasks.get() < this.mnFinishedProductsSum;
    }

    @Override
    public Long require() {
        long index = this.mFinishedTasks.getAndIncrement();
        if( index < this.mnFinishedProductsSum ){
            return index;
        }
        return null;
    }

    @Override
    public void deactivate( Long that ){
        this.mTaskCountDownLatch.countDown();
    }

    @Override
    public boolean isFinished() {
        return this.mTaskCountDownLatch.getCount() <= 0;
    }

    @Override
    public void awaitProducerFinished() throws InterruptedException {
        this.mTaskCountDownLatch.await();
    }

    @Override
    public void awaitProducerFinished( long timeout, TimeUnit unit ) throws InterruptedException {
        this.mTaskCountDownLatch.await( timeout, unit );
    }

}
