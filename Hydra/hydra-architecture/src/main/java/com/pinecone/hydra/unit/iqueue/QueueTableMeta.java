package com.pinecone.hydra.unit.iqueue;

public class QueueTableMeta implements QueueMeta {
    protected String  mszQueueTableName;

    public QueueTableMeta( String queueTableName ) {
        this.mszQueueTableName = queueTableName;
    }

    public QueueTableMeta(){}

    public void setQueueTableName( String queueTableName ) {
        this.mszQueueTableName = queueTableName;
    }

    @Override
    public String getQueueTable() {
        return this.mszQueueTableName;
    }
}
