package com.walnut.odin.formation.flow;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.walnut.odin.formation.plan.FormationProduct;

public abstract class ArchFormationProducer<T extends FormationProduct> implements FormationProducer<T> {
    protected String         mszClaimOwner;
    protected long           mnProductsSum;
    protected CountDownLatch mFinishedLatch;
    protected AtomicBoolean  mTerminateSignal = new AtomicBoolean( false );

    protected ArchFormationProducer( String claimOwner, long productsSum ) {
        this.mszClaimOwner = claimOwner;
        this.mnProductsSum = productsSum;
        this.mFinishedLatch = new CountDownLatch( (int)Math.min( productsSum, Integer.MAX_VALUE ) );
    }

    @Override
    public boolean hasTerminateSignal() {
        return this.mTerminateSignal.get();
    }

    public void terminate() {
        this.mTerminateSignal.set( true );
    }

    @Override
    public long getProductsSum() {
        return this.mnProductsSum;
    }

    @Override
    public void awaitProducerFinished() throws InterruptedException {
        this.mFinishedLatch.await();
    }

    @Override
    public void awaitProducerFinished( long timeout, TimeUnit unit ) throws InterruptedException {
        this.mFinishedLatch.await( timeout, unit );
    }

    @Override
    public boolean isFinished() {
        return this.mFinishedLatch.getCount() <= 0;
    }

    protected void countDown() {
        this.mFinishedLatch.countDown();
    }
}
