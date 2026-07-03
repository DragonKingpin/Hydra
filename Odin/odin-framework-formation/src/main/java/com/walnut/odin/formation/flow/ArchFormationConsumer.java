package com.walnut.odin.formation.flow;

import com.walnut.odin.formation.plan.FormationProduct;

public abstract class ArchFormationConsumer<T extends FormationProduct> implements FormationConsumer {
    protected FormationProducer<T>       mProducer;
    protected FormationFrameConsumerAdapter mFrameConsumerAdapter;

    protected ArchFormationConsumer( FormationProducer<T> producer, FormationFrameConsumerAdapter frameConsumerAdapter ) {
        this.mProducer = producer;
        this.mFrameConsumerAdapter = frameConsumerAdapter;
    }

    @Override
    public FormationProducer<T> producer() {
        return this.mProducer;
    }
}
