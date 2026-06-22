package com.walnut.odin.formation.strategy.preemptive;

import com.pinecone.slime.chunk.flow.frame.PreemptiveFrameFlowStrategy;
import com.walnut.odin.formation.FormationStrategyContext;
import com.walnut.odin.formation.FormationStrategyRuntime;
import com.walnut.odin.formation.FormationStrategy;

public interface PreemptiveFrameFormationStrategy extends FormationStrategy, PreemptiveFrameFlowStrategy {
    @Override
    PreemptiveFrameFormationProducer formulateProducer( FormationStrategyContext context, FormationStrategyRuntime runtime );

    @Override
    PreemptiveFrameFormationConsumer formulateConsumer( FormationStrategyContext context, FormationStrategyRuntime runtime );
}
