package com.walnut.redstone.ether.shuttle.kernel;

import com.pinecone.framework.system.prototype.Pinenut;

public class KernelMappedFile implements Pinenut {
    protected KernelMappedFileMeta mMeta;
    protected Object               mValue;

    public KernelMappedFileMeta getMeta() {
        return this.mMeta;
    }

    public void setMeta( KernelMappedFileMeta meta ) {
        this.mMeta = meta;
    }

    public Object getValue() {
        return this.mValue;
    }

    public void setValue( Object value ) {
        this.mValue = value;
    }
}
