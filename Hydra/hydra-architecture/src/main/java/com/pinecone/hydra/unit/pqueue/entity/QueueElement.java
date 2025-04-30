package com.pinecone.hydra.unit.pqueue.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface QueueElement extends Pinenut {

    long getEnumId();

    GUID getObjectGuid();

    long getPriority();

    long getLinkedPriority();

    long getIndexPriority();

    long getActualPriority();

    long getBias();

}
