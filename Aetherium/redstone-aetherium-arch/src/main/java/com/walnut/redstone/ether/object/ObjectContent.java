package com.walnut.redstone.ether.object;

import java.io.IOException;
import java.io.InputStream;

import com.pinecone.framework.system.prototype.Pinenut;

public class ObjectContent implements Pinenut {
    protected InputStream inputStream;
    protected ObjectContentSource contentSource;
    protected ObjectMetadata metadata;
    protected ObjectRange range;
    protected Long size;
    protected String contentType;

    public InputStream getInputStream() throws IOException {
        if ( this.inputStream == null && this.contentSource != null ) {
            return this.contentSource.openStream();
        }
        return this.inputStream;
    }

    public void setInputStream( InputStream inputStream ) {
        this.inputStream = inputStream;
    }

    public ObjectContentSource getContentSource() {
        return this.contentSource;
    }

    public void setContentSource( ObjectContentSource contentSource ) {
        this.contentSource = contentSource;
    }

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata( ObjectMetadata metadata ) {
        this.metadata = metadata;
    }

    public ObjectRange getRange() {
        return this.range;
    }

    public void setRange( ObjectRange range ) {
        this.range = range;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize( Long size ) {
        this.size = size;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType( String contentType ) {
        this.contentType = contentType;
    }
}
