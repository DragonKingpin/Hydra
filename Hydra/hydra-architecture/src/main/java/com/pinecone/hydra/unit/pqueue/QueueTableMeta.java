package com.pinecone.hydra.unit.pqueue;

public class QueueTableMeta implements QueueMeta {
    protected String  mszQueueTableName;

    public QueueTableMeta( String queueTableName ) {
        this.mszQueueTableName = queueTableName;
    }

    public QueueTableMeta(){}

    void setQueueTableName( String queueTableName ) {
        this.mszQueueTableName = queueTableName;
    }

    String queueTableName() {
        return this.mszQueueTableName;
    }
}
