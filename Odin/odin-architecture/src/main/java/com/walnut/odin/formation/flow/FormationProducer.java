package com.walnut.odin.formation.flow;

import java.util.concurrent.TimeUnit;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.formation.plan.FormationProduct;

public interface FormationProducer<T extends FormationProduct> extends Pinenut {
    T require();

    void deactivate( T product );

    boolean hasMoreProducts();

    boolean hasTerminateSignal();

    long getProductsSum();

    void awaitProducerFinished() throws InterruptedException;

    void awaitProducerFinished( long timeout, TimeUnit unit ) throws InterruptedException;

    boolean isFinished();
}
