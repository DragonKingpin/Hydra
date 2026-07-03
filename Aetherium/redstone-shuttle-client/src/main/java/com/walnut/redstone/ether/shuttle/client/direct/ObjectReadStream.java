package com.walnut.redstone.ether.shuttle.client.direct;

import java.io.IOException;
import java.io.InputStream;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectMetadata;

public class ObjectReadStream extends InputStream implements Pinenut {
    protected final InputStream inputStream;
    protected final String sourceUri;
    protected final ObjectMetadata metadata;

    public ObjectReadStream( InputStream inputStream, String sourceUri, ObjectMetadata metadata ) {
        if ( inputStream == null ) {
            throw new IllegalArgumentException( "Object read stream is required." );
        }
        this.inputStream = inputStream;
        this.sourceUri = sourceUri;
        this.metadata = metadata;
    }

    public String getSourceUri() {
        return this.sourceUri;
    }

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public String getFileName() {
        if ( this.metadata != null && this.metadata.getName() != null ) {
            return this.metadata.getName();
        }
        return null;
    }

    public Long getContentLength() {
        if ( this.metadata == null ) {
            return null;
        }
        return this.metadata.getSize();
    }

    public String getContentType() {
        if ( this.metadata == null ) {
            return null;
        }
        return this.metadata.getContentType();
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
