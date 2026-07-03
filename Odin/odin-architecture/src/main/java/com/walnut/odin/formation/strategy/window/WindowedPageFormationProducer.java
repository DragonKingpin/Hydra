package com.walnut.odin.formation.strategy.window;

import com.pinecone.slime.chunk.flow.window.WindowedPageFlowProducer;
import com.walnut.odin.formation.flow.FormationProducer;
import com.walnut.odin.formation.plan.FormationPage;

public interface WindowedPageFormationProducer extends FormationProducer<FormationPage>, WindowedPageFlowProducer {
    @Override
    default FormationPage produce() {
        return this.require();
    }
}
