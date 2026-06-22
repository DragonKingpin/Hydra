package com.walnut.odin.formation.plan;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitResult;

public interface FormationFrameFeedback extends Pinenut {
    FormationFrame frame();

    TaskInstantaneousSubmitResult submitResult();

    boolean accepted();

    boolean suspended();

    boolean rejected();
}
