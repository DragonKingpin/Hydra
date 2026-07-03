package com.walnut.odin.formation.strategy.window;

import com.pinecone.slime.chunk.flow.window.WindowedPageFlowConsumer;
import com.walnut.odin.formation.flow.FormationConsumer;

public interface WindowedPageFormationConsumer extends FormationConsumer, WindowedPageFlowConsumer {
    @Override
    WindowedPageFormationProducer producer();
}
