package com.walnut.odin.formation.strategy.preemptive;

import com.pinecone.slime.chunk.flow.frame.FrameFlowProducer;
import com.walnut.odin.formation.flow.FormationProducer;
import com.walnut.odin.formation.plan.FormationFrame;

public interface PreemptiveFrameFormationProducer extends FormationProducer<FormationFrame>, FrameFlowProducer {
    @Override
    default FormationFrame produce() {
        return this.require();
    }
}
