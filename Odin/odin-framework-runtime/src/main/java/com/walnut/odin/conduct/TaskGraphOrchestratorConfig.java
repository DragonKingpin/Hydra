package com.walnut.odin.conduct;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskGraphOrchestratorConfig extends Pinenut {

    String getQueueNodesTableName();

    String getTemporaryQueueNodesTableName();

}
