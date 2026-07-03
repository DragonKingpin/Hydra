package com.walnut.odin.formation.strategy.fixed;

import com.pinecone.slime.chunk.flow.page.PageFlowProducer;
import com.walnut.odin.formation.flow.FormationProducer;
import com.walnut.odin.formation.plan.FormationPage;

public interface FixedPageFormationProducer extends FormationProducer<FormationPage>, PageFlowProducer {
    @Override
    default FormationPage produce() {
        return this.require();
    }
}
