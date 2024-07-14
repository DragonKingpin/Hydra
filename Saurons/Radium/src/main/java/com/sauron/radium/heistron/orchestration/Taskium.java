package com.sauron.radium.heistron.orchestration;

import com.pinecone.framework.system.executum.Processum;

public interface Taskium extends Processum {
    default ChildHeistOrchestrator getHeistletOrchestrator() {
        return (ChildHeistOrchestrator) this.getTaskManager();
    }

    int getTaskId();
}
