package com.pinecone.hydra.grpc.client;

import java.util.Map;

import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.json.JSONObject;

public class GrpcClientConfig extends JSONConfig implements PatriarchalConfig {

    private final String  host;
    private final int     port;
    private final boolean enable;

    private final long    idleTimeoutMillis;
    private final long    keepAliveTimeSeconds;

    private final boolean autoReconnect;

    private final boolean enableHeartbeat;
    private final long    heartbeatIntervalMillis;


    public GrpcClientConfig( JSONConfig parent ) {
        this( (Map<String, Object>) null, parent );
    }

    public GrpcClientConfig( JSONObject thisScope, JSONConfig parent ) {
        this( thisScope.getMap(), parent );
    }

    public GrpcClientConfig( JSONObject thisScope ) {
        this( thisScope.getMap(), null );
    }

    public GrpcClientConfig( Map<String, Object> thisScope, JSONConfig parent ) {
        super( thisScope, parent );

        this.host = this.optString( "host", "localhost" );
        this.port = this.optInt( "port", 5888 );
        this.enable = this.optBoolean( "enable", true );

        this.idleTimeoutMillis = this.optLong( "idleTimeoutMillis", 30L );

        this.keepAliveTimeSeconds = this.optLong( "keepAliveTimeoutSec", 30L );

        this.autoReconnect = this.optBoolean( "autoReconnect", true );
        this.enableHeartbeat = this.optBoolean( "enableHeartbeat", false );
        this.heartbeatIntervalMillis = this.optLong( "heartbeatIntervalMillis", 2000L );
    }

    public GrpcClientConfig() {
        this( null );
    }


    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public long getIdleTimeoutMillis() {
        return this.idleTimeoutMillis;
    }

    public long getKeepAliveTimeSeconds() {
        return this.keepAliveTimeSeconds;
    }

    public boolean isAutoReconnect() {
        return this.autoReconnect;
    }

    public boolean isEnableHeartbeat() {
        return this.enableHeartbeat;
    }

    public long getHeartbeatIntervalMillis() {
        return this.heartbeatIntervalMillis;
    }


}
