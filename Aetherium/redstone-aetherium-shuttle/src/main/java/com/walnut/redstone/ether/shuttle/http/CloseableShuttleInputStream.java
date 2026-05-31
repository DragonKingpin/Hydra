package com.walnut.redstone.ether.shuttle.http;

import java.io.IOException;
import java.io.InputStream;

import com.pinecone.framework.system.prototype.Pinenut;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

public class CloseableShuttleInputStream extends InputStream implements Pinenut {
    protected final InputStream bodyStream;
    protected final CloseableHttpResponse response;
    protected boolean closed;

    public CloseableShuttleInputStream( InputStream bodyStream, CloseableHttpResponse response ) {
        this.bodyStream = bodyStream;
        this.response = response;
    }

    @Override
    public int read() throws IOException {
        return this.bodyStream.read();
    }

    @Override
    public int read( byte[] buffer, int offset, int length ) throws IOException {
        return this.bodyStream.read( buffer, offset, length );
    }

    @Override
    public long skip( long n ) throws IOException {
        return this.bodyStream.skip( n );
    }

    @Override
    public int available() throws IOException {
        return this.bodyStream.available();
    }

    @Override
    public void close() throws IOException {
        if ( this.closed ) {
            return;
        }
        this.closed = true;
        IOException thrown = null;
        try {
            this.bodyStream.close();
        }
        catch ( IOException ex ) {
            thrown = ex;
        }
        try {
            this.response.close();
        }
        catch ( IOException ex ) {
            if ( thrown == null ) {
                thrown = ex;
            }
            else {
                thrown.addSuppressed( ex );
            }
        }
        if ( thrown != null ) {
            throw thrown;
        }
    }
}
