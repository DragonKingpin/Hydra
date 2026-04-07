package com.pinecone.hydra.unit.iqueue;

public abstract class ArchQueueTableMeta implements QueueMeta {
    protected String  mszQueueTableName;

    public ArchQueueTableMeta(String queueTableName ) {
        this.mszQueueTableName = queueTableName;
    }

    public ArchQueueTableMeta(){}

    public void setQueueTableName( String queueTableName ) {
        this.mszQueueTableName = queueTableName;
    }

    @Override
    public String getQueueTable() {
        return this.mszQueueTableName;
    }
}
