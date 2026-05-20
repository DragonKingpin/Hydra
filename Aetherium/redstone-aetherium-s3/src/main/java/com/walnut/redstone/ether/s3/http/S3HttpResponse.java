package com.walnut.redstone.ether.s3.http;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class S3HttpResponse implements Pinenut {
    protected int statusCode = 200;
    protected String body;
    protected final Map<String, String> headers = new LinkedHashMap<>();

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode( int statusCode ) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody( String body ) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeader( String name, String value ) {
        if ( name == null || name.trim().isEmpty() || value == null ) {
            return;
        }
        this.headers.put( name, value );
    }
}
