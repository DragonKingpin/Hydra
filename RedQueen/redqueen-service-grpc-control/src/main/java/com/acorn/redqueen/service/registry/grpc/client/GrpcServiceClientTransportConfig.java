package com.acorn.redqueen.service.registry.grpc.client;

import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.json.JSONObject;

public class GrpcServiceClientTransportConfig extends JSONConfig implements PatriarchalConfig, Pinenut {

    protected long mnControlSyncTimeoutMillis;

    protected long mnCommandTimeoutMillis;

    protected boolean mbEnableHeartbeat;

    protected long mnHeartbeatIntervalMillis;

    public GrpcServiceClientTransportConfig() {
        this( (Map<String, Object>) null, null );
    }

    public GrpcServiceClientTransportConfig( JSONObject thisScope ) {
        this( thisScope == null ? null : thisScope.getMap(), null );
    }

    public GrpcServiceClientTransportConfig( Map<String, Object> thisScope, JSONConfig parent ) {
        super( thisScope, parent );
        this.mnControlSyncTimeoutMillis = this.optLong( "controlSyncTimeoutMillis", 5000L );
        this.mnCommandTimeoutMillis = this.optLong( "commandTimeoutMillis", 30000L );
        this.mbEnableHeartbeat = this.optBoolean( "enableHeartbeat", false );
        this.mnHeartbeatIntervalMillis = this.optLong( "heartbeatIntervalMillis", 2000L );
    }

    public long getControlSyncTimeoutMillis() {
        return this.mnControlSyncTimeoutMillis;
    }

    public long getCommandTimeoutMillis() {
        return this.mnCommandTimeoutMillis;
    }

    public boolean isEnableHeartbeat() {
        return this.mbEnableHeartbeat;
    }

    public long getHeartbeatIntervalMillis() {
        return this.mnHeartbeatIntervalMillis;
    }

}



