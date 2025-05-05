package com.pinecone.hydra.atlas.advance;

import com.pinecone.framework.util.id.GUID;

public class GenericQueueEntity implements QueueEntity{
    private GUID mGuid;

    private int mnStratum;



    public GenericQueueEntity(){}

    public GenericQueueEntity( GUID guid, int stratum ) {
        this.mGuid = guid;
        this.mnStratum = stratum;
    }

    @Override
    public void setGuid(GUID guid) {
        this.mGuid = guid;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public void setStratum(int stratum) {
        this.mnStratum = stratum;
    }

    @Override
    public int getStratum() {
        return this.mnStratum;
    }
}
