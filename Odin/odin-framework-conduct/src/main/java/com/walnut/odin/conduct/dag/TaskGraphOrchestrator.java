package com.walnut.odin.conduct.dag;

import com.pinecone.framework.system.regime.Orchestrator;

public interface TaskGraphOrchestrator extends Orchestrator {
    void execute();
}
