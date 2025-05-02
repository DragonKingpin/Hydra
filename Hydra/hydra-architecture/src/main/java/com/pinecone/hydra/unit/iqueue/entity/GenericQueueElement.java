package com.pinecone.hydra.unit.iqueue.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;

public class GenericQueueElement implements QueueElement {

    private long        mnEnumId;

    private GUID        mObjectGuid;

    private long        mnPriority;

    private long        mnLinkedPriority;

    private long        mnIndexPriority;

    private double      mBias;

    @Override
    public long getEnumId() {
        return 0;
    }

    public void setEnumId( long enumId ) {
        this.mnEnumId = enumId;
    }

    @Override
    public GUID getObjectGuid() {
        return this.mObjectGuid;
    }

    public void setObjectGuid( GUID objectGuid ) {
        this.mObjectGuid = objectGuid;
    }


    @Override
    public long getPriority() {
        return this.mnPriority;
    }

    public void setPriority( long priority ) {
        this.mnPriority = priority;
    }

    @Override
    public long getLinkedPriority() {
        return this.mnLinkedPriority;
    }

    public void setLinkedPriority( long linkedPriority ) {
        this.mnLinkedPriority = linkedPriority;
    }

    @Override
    public long getIndexPriority() {
        return this.mnIndexPriority;
    }

    public void setIndexPriority( long indexPriority ) {
        this.mnIndexPriority = indexPriority;
    }

    @Override
    public long getActualPriority() {
        return 0;
    }

    @Override
    public double getBias() {
        return this.mBias;
    }

    public void  setBias( double bias ) {
        this.mBias = bias;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
