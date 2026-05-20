package com.walnut.redstone.ether.shuttle.exchange;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleRequest implements Pinenut {
    protected ShuttleMethod method;
    protected String targetName;
    protected String path;
    protected String queryString;
    protected Map<String, String> headers = new LinkedHashMap<>();
    protected InputStream bodyStream;
    protected Long contentLength;

    public ShuttleMethod getMethod() {
        return this.method;
    }

    public void setMethod( ShuttleMethod method ) {
        this.method = method;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName( String targetName ) {
        this.targetName = targetName;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString( String queryString ) {
        this.queryString = queryString;
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

    public Long getContentLength() {
        return this.contentLength;
    }

    public void setContentLength( Long contentLength ) {
        this.contentLength = contentLength;
    }
}
