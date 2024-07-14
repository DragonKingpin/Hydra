package com.sauron.radium.heistron.scheduler;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.concurrent.TimeUnit;

public interface TaskProducer extends Pinenut {
    Object require();

    boolean hasMoreProducts();

    boolean hasTerminateSignal();

    long getProductsSum();

    void awaitProducerFinished() throws InterruptedException;

    void awaitProducerFinished( long timeout, TimeUnit unit ) throws InterruptedException;

    boolean isFinished();
}
