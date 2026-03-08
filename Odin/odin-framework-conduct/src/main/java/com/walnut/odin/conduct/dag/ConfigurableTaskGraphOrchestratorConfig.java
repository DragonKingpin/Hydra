package com.walnut.odin.conduct.dag;

public class ConfigurableTaskGraphOrchestratorConfig implements TaskGraphOrchestratorConfig {

    private String queueNodesTableName;
    private String temporaryQueueNodesTableName;

    public ConfigurableTaskGraphOrchestratorConfig() {
        this.queueNodesTableName          = TaskGraphOrchestratorConstants.STANDARD_GLOBAL_QUEUE_NODES_TABLE;
        this.temporaryQueueNodesTableName = TaskGraphOrchestratorConstants.STANDARD_GLOBAL_TEMPORARY_QUEUE_NODES_TABLE;
    }

    @Override
    public String getQueueNodesTableName() {
        return this.queueNodesTableName;
    }

    @Override
    public String getTemporaryQueueNodesTableName() {
        return this.temporaryQueueNodesTableName;
    }

    public void setQueueNodesTableName( String queueNodesTableName ) {
        this.queueNodesTableName = queueNodesTableName;
    }

    public void setTemporaryQueueNodesTableName( String temporaryQueueNodesTableName ) {
        this.temporaryQueueNodesTableName = temporaryQueueNodesTableName;
    }

}
