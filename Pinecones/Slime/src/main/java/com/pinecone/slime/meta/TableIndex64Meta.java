package com.pinecone.slime.meta;

public class TableIndex64Meta implements TableIndexMeta {

    private long mnMinId;
    private long mnMaxId;

    public TableIndex64Meta( long nMinId, long nMaxId ) {
        this.mnMinId = nMinId;
        this.mnMaxId = nMaxId;
    }

    @Override
    public long getMinId() {
        return this.mnMinId;
    }

    @Override
    public long getMaxId() {
        return this.mnMaxId;
    }

    public void setMaxId( long nMaxId ) {
        this.mnMaxId = nMaxId;
    }

    public void setMinId( long nMinId ) {
        this.mnMinId = nMinId;
    }

}
