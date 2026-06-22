package com.walnut.odin.formation.strategy.fixed;

import com.pinecone.slime.chunk.flow.page.FixedPageFlowStrategy;
import com.walnut.odin.formation.FormationStrategyContext;
import com.walnut.odin.formation.FormationStrategyRuntime;
import com.walnut.odin.formation.FormationStrategy;

public interface FixedPageFormationStrategy extends FormationStrategy, FixedPageFlowStrategy {
    @Override
    FixedPageFormationProducer formulateProducer( FormationStrategyContext context, FormationStrategyRuntime runtime );

    @Override
    FixedPageFormationConsumer formulateConsumer( FormationStrategyContext context, FormationStrategyRuntime runtime );
}
