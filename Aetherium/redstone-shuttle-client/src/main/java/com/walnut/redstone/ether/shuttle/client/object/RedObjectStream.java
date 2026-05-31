package com.walnut.redstone.ether.shuttle.client.object;

import java.io.IOException;
import java.io.InputStream;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectMetadata;

public class RedObjectStream extends InputStream implements Pinenut {
    protected final InputStream inputStream;
    protected final ObjectMetadata metadata;

    public RedObjectStream( InputStream inputStream, ObjectMetadata metadata ) {
        this.inputStream = inputStream;
        this.metadata = metadata;
    }

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    public int read() throws IOException {
        return this.inputStream.read();
    }

    @Override
    public int read( byte[] buffer, int offset, int length ) throws IOException {
        return this.inputStream.read( buffer, offset, length );
    }

    @Override
    public long skip( long n ) throws IOException {
        return this.inputStream.skip( n );
    }

    @Override
    public int available() throws IOException {
        return this.inputStream.available();
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
