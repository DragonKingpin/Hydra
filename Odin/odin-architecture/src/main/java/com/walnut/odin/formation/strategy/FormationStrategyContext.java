package com.walnut.odin.formation.strategy;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;

public interface FormationStrategyContext extends Pinenut {
    GUID formationGuid();

    GUID groupGuid();

    UniformTaskScheduler taskScheduler();
}
