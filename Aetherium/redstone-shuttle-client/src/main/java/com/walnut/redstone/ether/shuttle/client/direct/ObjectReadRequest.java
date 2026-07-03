package com.walnut.redstone.ether.shuttle.client.direct;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class ObjectReadRequest implements Pinenut {
    protected URI uri;
    protected String rawUri;
    protected Map<String, String> headers = new LinkedHashMap<>();

    public static ObjectReadRequest of( String rawUri ) {
        ObjectReadRequest ret = new ObjectReadRequest();
        ret.setUri( rawUri );
        return ret;
    }

    public static ObjectReadRequest of( URI uri ) {
        ObjectReadRequest ret = new ObjectReadRequest();
        ret.setUri( uri );
        return ret;
    }

    public URI getUri() {
        return this.uri;
    }

    public void setUri( URI uri ) {
        if ( uri == null ) {
            throw new IllegalArgumentException( "Object read uri is required." );
        }
        this.uri = uri;
        this.rawUri = uri.toString();
    }

    public String getRawUri() {
        return this.rawUri;
    }

    public void setUri( String rawUri ) {
        if ( rawUri == null || rawUri.trim().isEmpty() ) {
            throw new IllegalArgumentException( "Object read uri is required." );
        }
        this.rawUri = rawUri.trim();
        this.uri = URI.create( this.rawUri );
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders( Map<String, String> headers ) {
        this.headers = headers == null ? new LinkedHashMap<>() : new LinkedHashMap<>( headers );
    }

    public String scheme() {
        String scheme = this.uri.getScheme();
        if ( scheme == null ) {
            return "";
        }
        return scheme.toLowerCase( Locale.ROOT );
    }
}
