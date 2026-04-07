package com.walnut.odin.conduct.dag;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskGraphOrchestratorConfig extends Pinenut {

    String getQueueNodesTableName();

    String getTemporaryQueueNodesTableName();

}
