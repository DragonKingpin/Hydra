package com.walnut.redstone.ether.shuttle.exchange;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleResponse implements Pinenut {
    protected int statusCode;
    protected Map<String, String> headers = new LinkedHashMap<>();
    protected InputStream bodyStream;

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode( int statusCode ) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders( Map<String, String> headers ) {
        this.headers = headers == null ? new LinkedHashMap<>() : new LinkedHashMap<>( headers );
    }

    public InputStream getBodyStream() {
        return this.bodyStream;
    }

    public void setBodyStream( InputStream bodyStream ) {
        this.bodyStream = bodyStream;
    }
}
