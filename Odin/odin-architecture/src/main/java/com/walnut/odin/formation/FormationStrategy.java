package com.walnut.odin.formation;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.formation.flow.FormationConsumer;
import com.walnut.odin.formation.flow.FormationProducer;

public interface FormationStrategy extends Pinenut {
    FormationStrategyType kind();

    void prepare( FormationStrategyContext context, FormationStrategyRuntime runtime );

    FormationProducer<?> formulateProducer( FormationStrategyContext context, FormationStrategyRuntime runtime );

    FormationConsumer formulateConsumer( FormationStrategyContext context, FormationStrategyRuntime runtime );

    boolean hasTerminateSignal( FormationStrategyContext context, FormationStrategyRuntime runtime );

    boolean isFinished( FormationStrategyContext context, FormationStrategyRuntime runtime );
}
