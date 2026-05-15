package com.pinecone.hydra.storage.file.transmit.channel;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.OutputStream;

public final class UFileReadRequest implements Pinenut {
    protected final long         mPosition;
    protected final long         mLength;
    protected final OutputStream mOutputStream;

    public UFileReadRequest( long position, long length, OutputStream outputStream ) {
        this.mPosition     = position;
        this.mLength       = length;
        this.mOutputStream = outputStream;
    }

    public long getPosition() {
        return this.mPosition;
    }

    public long getLength() {
        return this.mLength;
    }

    public OutputStream getOutputStream() {
        return this.mOutputStream;
    }
}
