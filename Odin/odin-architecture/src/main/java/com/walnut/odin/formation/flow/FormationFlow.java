package com.walnut.odin.formation.flow;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.formation.FormationStrategy;
import com.walnut.odin.formation.FormationStrategyContext;
import com.walnut.odin.formation.FormationStrategyRuntime;

public interface FormationFlow extends Pinenut {
    FormationStrategy strategy();

    FormationProducer<?> producer();

    FormationConsumer consumer();

    FormationStrategyRuntime runtime();

    void prepare( FormationStrategyContext context );

    void flow( FormationStrategyContext context );

    boolean isFinished();
}
