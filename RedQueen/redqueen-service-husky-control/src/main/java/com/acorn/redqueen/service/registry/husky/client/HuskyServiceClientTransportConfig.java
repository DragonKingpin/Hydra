package com.acorn.redqueen.service.registry.husky.client;

import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.json.JSONObject;

public class HuskyServiceClientTransportConfig extends JSONConfig implements PatriarchalConfig, Pinenut {

    protected long mnControlSyncTimeoutMillis;

    public HuskyServiceClientTransportConfig() {
        this( (Map<String, Object>) null, null );
    }

    public HuskyServiceClientTransportConfig( JSONObject thisScope ) {
        this( thisScope == null ? null : thisScope.getMap(), null );
    }

    public HuskyServiceClientTransportConfig( Map<String, Object> thisScope, JSONConfig parent ) {
        super( thisScope, parent );
        this.mnControlSyncTimeoutMillis = this.optLong( "controlSyncTimeoutMillis", 5000L );
    }

    public long getControlSyncTimeoutMillis() {
        return this.mnControlSyncTimeoutMillis;
    }

}
