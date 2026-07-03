package com.walnut.odin.formation.strategy.window;

import com.pinecone.slime.chunk.flow.window.WindowedPageFlowStrategy;
import com.walnut.odin.formation.strategy.FormationStrategyContext;
import com.walnut.odin.formation.strategy.FormationStrategyRuntime;
import com.walnut.odin.formation.strategy.FormationStrategy;

public interface WindowedPageFormationStrategy extends FormationStrategy, WindowedPageFlowStrategy {
    @Override
    WindowedPageFormationProducer formulateProducer( FormationStrategyContext context, FormationStrategyRuntime runtime );

    @Override
    WindowedPageFormationConsumer formulateConsumer( FormationStrategyContext context, FormationStrategyRuntime runtime );
}
