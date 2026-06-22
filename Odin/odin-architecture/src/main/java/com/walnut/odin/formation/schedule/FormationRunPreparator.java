package com.walnut.odin.formation.schedule;

import com.pinecone.framework.system.regime.Executioner;
import com.walnut.odin.formation.entity.FormationRunSubmitRequest;

public interface FormationRunPreparator extends Executioner {

    Object prepareRun( FormationRunSubmitRequest request );
}
