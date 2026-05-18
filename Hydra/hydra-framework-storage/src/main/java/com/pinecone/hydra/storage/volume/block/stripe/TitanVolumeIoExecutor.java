package com.pinecone.hydra.storage.volume.block.stripe;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TitanVolumeIoExecutor implements VolumeIoExecutor {
    protected static final AtomicInteger  THREAD_SEQUENCE = new AtomicInteger( 0 );
    protected static final ExecutorService EXECUTOR = Executors.newCachedThreadPool( new StripeThreadFactory() );

    @Override
    public StripeIoResult execute( StripedIoPlan plan, ByteBuffer buffer, boolean write ) throws IOException {
        if ( plan.getTotalLength() <= 0 ) {
            return new StripeIoResult( 0 );
        }
        if ( plan.getActiveLaneCount() <= 1 ) {
            return new StripeIoResult( this.executeInline( plan, buffer, write ) );
        }
        return new StripeIoResult( this.executeParallel( plan, buffer, write ) );
    }

    protected int executeInline( StripedIoPlan plan, ByteBuffer buffer, boolean write ) throws IOException {
        int transferred = 0;
        for ( StripeLane lane : plan.getLanes() ) {
            if ( !lane.isEmpty() ) {
                transferred += this.executeLane( lane, buffer, write, null );
            }
        }
        buffer.position( plan.getBufferPosition() + transferred );
        return transferred;
    }

    protected int executeParallel( StripedIoPlan plan, ByteBuffer buffer, boolean write ) throws IOException {
        AtomicInteger transferred = new AtomicInteger( 0 );
        AtomicInteger remainingLanes = new AtomicInteger( plan.getActiveLaneCount() );
        AtomicReference<Throwable> firstFailure = new AtomicReference<>();
        CountDownLatch doneLatch = new CountDownLatch( plan.getActiveLaneCount() );
        for ( StripeLane lane : plan.getLanes() ) {
            if ( lane.isEmpty() ) {
                continue;
            }
            EXECUTOR.execute( () -> {
                try {
                    transferred.addAndGet( this.executeLane( lane, buffer, write, firstFailure ) );
                }
                catch ( Throwable e ) {
                    firstFailure.compareAndSet( null, e );
                }
                finally {
                    remainingLanes.decrementAndGet();
                    doneLatch.countDown();
                }
            } );
        }
        this.await( doneLatch );
        Throwable failure = firstFailure.get();
        if ( failure != null ) {
            if ( failure instanceof IOException ) {
                throw (IOException)failure;
            }
            throw new IOException( "Striped volume IO failed", failure );
        }
        if ( transferred.get() != plan.getTotalLength() ) {
            throw new IOException(
                    "Short striped " + ( write ? "write" : "read" ) + ", expected "
                            + plan.getTotalLength() + " but got " + transferred.get()
            );
        }
        buffer.position( plan.getBufferPosition() + transferred.get() );
        return transferred.get();
    }

    protected int executeLane(
            StripeLane lane,
            ByteBuffer buffer,
            boolean write,
            AtomicReference<Throwable> firstFailure
    ) throws IOException {
        int transferred = 0;
        for ( StripeSlice slice : lane.getSlices() ) {
            if ( firstFailure != null && firstFailure.get() != null ) {
                break;
            }
            int sliceTransferred = this.executeSlice( slice, buffer, write );
            if ( sliceTransferred != slice.getLength() ) {
                throw new IOException(
                        "Short striped " + ( write ? "write" : "read" ) + " on lane "
                                + lane.getChildIndex() + ", expected " + slice.getLength()
                                + " but got " + sliceTransferred
                );
            }
            transferred += sliceTransferred;
        }
        return transferred;
    }

    protected int executeSlice( StripeSlice stripeSlice, ByteBuffer buffer, boolean write ) throws IOException {
        ByteBuffer view = buffer.duplicate();
        view.position( stripeSlice.getBufferOffset() );
        view.limit( stripeSlice.getBufferOffset() + stripeSlice.getLength() );
        ByteBuffer slice = view.slice();
        return write
                ? stripeSlice.getChild().write( stripeSlice.getChildPosition(), slice )
                : stripeSlice.getChild().read( stripeSlice.getChildPosition(), slice );
    }

    protected void await( CountDownLatch latch ) throws IOException {
        try {
            latch.await();
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
            throw new IOException( "Striped volume IO interrupted", e );
        }
    }

    protected static class StripeThreadFactory implements ThreadFactory, Pinenut {
        @Override
        public Thread newThread( Runnable runnable ) {
            Thread thread = new Thread( runnable, "titan-stripe-io-" + THREAD_SEQUENCE.incrementAndGet() );
            thread.setDaemon( true );
            return thread;
        }
    }
}
