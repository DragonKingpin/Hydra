package com.pinecone.hydra.grpc.server;

import java.util.Map;

import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.json.JSONObject;

public class GrpcServerConfig extends JSONConfig implements PatriarchalConfig {

    private final String  host;
    private final int     port;
    private final boolean enabled;

    private final long    handshakeTimeoutMillis;
    private final long    keepAliveTimeSeconds;
    private final long    keepAliveTimeoutSeconds;

    private final int     maxConcurrentCalls;
    private final int     maxInboundMessageSize;
    private final int     maxInboundMetadataSize;

    private final boolean permitKeepAliveWithoutCalls;


    public GrpcServerConfig( JSONConfig parent ) {
        this( (Map<String, Object >) null, parent );
    }

    public GrpcServerConfig( JSONObject thisScope, JSONConfig parent ) {
        this( thisScope.getMap(), parent );
    }

    public GrpcServerConfig( JSONObject thisScope ) {
        this( thisScope.getMap(), null );
    }

    public GrpcServerConfig( Map<String, Object > thisScope, JSONConfig parent ) {
        super( thisScope, parent );

        this.host = this.optString( "host", "0.0.0.0" );
        this.port = this.optInt( "port", 5888 );
        this.enabled = this.optBoolean( "enable", true );

        this.handshakeTimeoutMillis = this.optLong( "handshakeTimeoutMillis", 0L );
        this.keepAliveTimeSeconds = this.optLong( "keepAliveTimeoutSec", 0L );

        this.keepAliveTimeoutSeconds = this.optLong( "keepAliveAckTimeoutSec", 20L );
        this.maxConcurrentCalls = this.optInt( "maximumConnections", Integer.MAX_VALUE );

        // gRPC inbound 限制
        this.maxInboundMessageSize = this.optInt( "maxInboundMessageSize", 4 * 1024 * 1024 );
        this.maxInboundMetadataSize = this.optInt( "maxInboundMetadataSize", 8 * 1024 );

        // 是否允许无调用时keepalive
        this.permitKeepAliveWithoutCalls = this.optBoolean( "permitKeepAliveWithoutCalls", true );
    }

    public GrpcServerConfig() {
        this(null );
    }



    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public long getHandshakeTimeoutMillis() {
        return this.handshakeTimeoutMillis;
    }

    public long getKeepAliveTimeSeconds() {
        return this.keepAliveTimeSeconds;
    }

    public long getKeepAliveTimeoutSeconds() {
        return this.keepAliveTimeoutSeconds;
    }

    public int getMaxConcurrentCalls() {
        return this.maxConcurrentCalls;
    }

    public int getMaxInboundMessageSize() {
        return this.maxInboundMessageSize;
    }

    public int getMaxInboundMetadataSize() {
        return this.maxInboundMetadataSize;
    }

    public boolean isPermitKeepAliveWithoutCalls() {
        return this.permitKeepAliveWithoutCalls;
    }

}
