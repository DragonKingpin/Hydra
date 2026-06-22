package com.walnut.odin.formation.strategy.preemptive;

import com.pinecone.slime.chunk.flow.frame.FrameFlowConsumer;
import com.walnut.odin.formation.flow.FormationConsumer;

public interface PreemptiveFrameFormationConsumer extends FormationConsumer, FrameFlowConsumer {
    @Override
    PreemptiveFrameFormationProducer producer();
}
