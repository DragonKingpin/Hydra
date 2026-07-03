package com.walnut.redstone.ether.shuttle.client.object;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class RedObjectPutOptions implements Pinenut {
    protected String contentType;
    protected Map<String, String> metadata = new LinkedHashMap<>();

    public static RedObjectPutOptions empty() {
        return new RedObjectPutOptions();
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType( String contentType ) {
        this.contentType = contentType;
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public void setMetadata( Map<String, String> metadata ) {
        this.metadata = metadata == null ? new LinkedHashMap<>() : new LinkedHashMap<>( metadata );
    }
}
