package com.pinecone.hydra.unit.iqueue.entity;

public interface QueueStratumElement extends QueueElement {
    void setStratum( short stratum  );

    short getStratum();
}
