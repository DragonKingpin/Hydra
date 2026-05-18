package com.pinecone.hydra.storage.file.transmit.channel;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.InputStream;

public final class UFileAppendRequest implements Pinenut {
    protected final InputStream mInputStream;
    protected final long        mSize;

    public UFileAppendRequest( InputStream inputStream, long size ) {
        this.mInputStream = inputStream;
        this.mSize        = size;
    }

    public InputStream getInputStream() {
        return this.mInputStream;
    }

    public long getSize() {
        return this.mSize;
    }
}
