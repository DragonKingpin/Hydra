package com.walnut.odin.formation.flow;

import com.pinecone.framework.system.prototype.Pinenut;

public interface FormationConsumer extends Pinenut {
    void consume();

    FormationProducer<?> producer();
}
