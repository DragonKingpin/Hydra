package com.pinecone.hydra.unit.iqueue.entity;

public class GenericStratumQueueElement extends GenericQueueElement implements QueueStratumElement{
    protected short mStratum;

    @Override
    public void setStratum(short stratum) {
        this.mStratum = stratum;
    }

    @Override
    public short getStratum() {
        return this.mStratum;
    }
}
