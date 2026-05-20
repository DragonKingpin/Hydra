package com.walnut.redstone.ether.object;

import com.pinecone.framework.system.prototype.Pinenut;

public class ObjectRange implements Pinenut {
    protected long start;
    protected long end;
    protected long length;
    protected boolean partial;

    public long getStart() {
        return this.start;
    }

    public void setStart( long start ) {
        this.start = start;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd( long end ) {
        this.end = end;
    }

    public long getLength() {
        return this.length;
    }

    public void setLength( long length ) {
        this.length = length;
    }

    public boolean isPartial() {
        return this.partial;
    }

    public void setPartial( boolean partial ) {
        this.partial = partial;
    }
}

