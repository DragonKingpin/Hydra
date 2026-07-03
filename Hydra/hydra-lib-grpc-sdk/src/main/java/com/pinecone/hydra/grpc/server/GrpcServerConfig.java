package com.pinecone.hydra.grpc.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONObject;

public class GrpcServerConfig extends JSONConfig implements PatriarchalConfig {

    private final String  host;
    private final int     port;
    private final boolean enabled;

    private final long    handshakeTimeoutMillis;
    private final long    keepAliveTimeSeconds;
    private final long    keepAliveTimeoutSeconds;
    private final boolean enableHeartbeat;
    private final long    heartbeatIntervalMillis;

    private final int     maxConcurrentCalls;
    private final int     maxInboundMessageSize;
    private final int     maxInboundMetadataSize;

    private final boolean permitKeepAliveWithoutCalls;

    private final String       additionalGrpcServicesCheckMode;
    private final List<String> additionalGrpcServiceClassNames;


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
        this.keepAliveTimeSeconds = this.containsKey( "keepAliveTimeSec" )
                ? this.optLong( "keepAliveTimeSec", 0L )
                : this.optLong( "keepAliveTimeoutSec", 0L );

        this.keepAliveTimeoutSeconds = this.containsKey( "keepAliveTimeSec" )
                ? this.optLong( "keepAliveTimeoutSec", 20L )
                : this.optLong( "keepAliveAckTimeoutSec", 20L );
        this.enableHeartbeat = this.optBoolean( "enableHeartbeat", false );
        this.heartbeatIntervalMillis = this.optLong( "heartbeatIntervalMillis", 2000L );
        this.maxConcurrentCalls = this.optInt( "maximumConnections", Integer.MAX_VALUE );

        // gRPC inbound 限制
        this.maxInboundMessageSize = this.optInt( "maxInboundMessageSize", 4 * 1024 * 1024 );
        this.maxInboundMetadataSize = this.optInt( "maxInboundMetadataSize", 8 * 1024 );

        // 是否允许无调用时keepalive
        this.permitKeepAliveWithoutCalls = this.optBoolean( "permitKeepAliveWithoutCalls", true );

        // additionalGrpcServices — 反射注册的额外 gRPC 服务
        JSONObject joAdditional = this.optJSONObject( "additionalGrpcServices" );
        if ( joAdditional != null ) {
            this.additionalGrpcServicesCheckMode = joAdditional.optString( "checkMode", "strict" );
            JSONArray jaServices = joAdditional.optJSONArray( "services" );
            if ( jaServices != null && jaServices.size() > 0 ) {
                List<String> classNames = new ArrayList<>( jaServices.size() );
                for ( int i = 0; i < jaServices.size(); ++i ) {
                    String szClassName = jaServices.optString( i, null );
                    if ( szClassName != null && !szClassName.trim().isEmpty() ) {
                        classNames.add( szClassName.trim() );
                    }
                }
                this.additionalGrpcServiceClassNames = Collections.unmodifiableList( classNames );
            }
            else {
                this.additionalGrpcServiceClassNames = Collections.emptyList();
            }
        }
        else {
            this.additionalGrpcServicesCheckMode    = "strict";
            this.additionalGrpcServiceClassNames    = Collections.emptyList();
        }
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

    public boolean isEnableHeartbeat() {
        return this.enableHeartbeat;
    }

    public long getHeartbeatIntervalMillis() {
        return this.heartbeatIntervalMillis;
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

    public String getAdditionalGrpcServicesCheckMode() {
        return this.additionalGrpcServicesCheckMode;
    }

    public List<String> getAdditionalGrpcServiceClassNames() {
        return this.additionalGrpcServiceClassNames;
    }

    public boolean isAdditionalGrpcServicesStrictMode() {
        return !"warn".equalsIgnoreCase( this.additionalGrpcServicesCheckMode );
    }

}
