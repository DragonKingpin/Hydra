package com.walnut.odin.formation.strategy.fixed;

import com.pinecone.slime.chunk.flow.page.PageFlowConsumer;
import com.walnut.odin.formation.flow.FormationConsumer;

public interface FixedPageFormationConsumer extends FormationConsumer, PageFlowConsumer {
    @Override
    FixedPageFormationProducer producer();
}
