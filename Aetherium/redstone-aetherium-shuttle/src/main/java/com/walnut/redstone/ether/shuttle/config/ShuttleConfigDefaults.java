package com.walnut.redstone.ether.shuttle.config;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.lifecycle.ShuttleKernel;

public class ShuttleConfigDefaults implements Pinenut {
    public ShuttleConfig apply( ShuttleConfig config ) {
        ShuttleConfig ret = config == null ? new ShuttleConfig() : config;
        if ( this.blank( ret.getName() ) ) {
            ret.setName( "red-shuttle" );
        }
        if ( this.blank( ret.getDefaultTarget() ) ) {
            ret.setDefaultTarget( "local-titan-aether" );
        }
        this.ensureChildren( ret );
        this.applyHttpClient( ret.getHttpClient() );
        this.applyPool( ret.getPool() );
        this.applyTimeout( ret.getTimeout() );
        this.applyHeaderPolicy( ret.getHeaderPolicy() );
        return ret;
    }

    protected void ensureChildren( ShuttleConfig config ) {
        if ( config.getHttpClient() == null ) {
            config.setHttpClient( new ShuttleHttpClientConfig() );
        }
        if ( config.getPool() == null ) {
            config.setPool( new ShuttlePoolConfig() );
        }
        if ( config.getTimeout() == null ) {
            config.setTimeout( new ShuttleTimeoutConfig() );
        }
        if ( config.getRetry() == null ) {
            config.setRetry( new ShuttleRetryConfig() );
        }
        if ( config.getHeaderPolicy() == null ) {
            config.setHeaderPolicy( new ShuttleHeaderPolicyConfig() );
        }
        if ( config.getProxy() == null ) {
            config.setProxy( new ShuttleProxyConfig() );
        }
        if ( config.getTls() == null ) {
            config.setTls( new ShuttleTlsConfig() );
        }
    }

    protected void applyHttpClient( ShuttleHttpClientConfig config ) {
        if ( config == null ) {
            return;
        }
        if ( this.blank( config.getType() ) ) {
            config.setType( ShuttleKernel.HttpClientTypeApacheHttpClient5ClassicStreaming );
        }
        if ( config.getIoThreadCount() <= 0 ) {
            config.setIoThreadCount( Math.max( Runtime.getRuntime().availableProcessors(), 2 ) );
        }
        if ( this.blank( config.getUserAgent() ) ) {
            config.setUserAgent( "RedShuttle/1.0" );
        }
    }

    protected void applyPool( ShuttlePoolConfig config ) {
        if ( config == null ) {
            return;
        }
        if ( config.getMaxTotalConnections() <= 0 ) {
            config.setMaxTotalConnections( 256 );
        }
        if ( config.getMaxConnectionsPerRoute() <= 0 ) {
            config.setMaxConnectionsPerRoute( 64 );
        }
        if ( config.getConnectionTimeToLiveMillis() <= 0L ) {
            config.setConnectionTimeToLiveMillis( 300000L );
        }
        if ( config.getEvictIdleConnectionsMillis() <= 0L ) {
            config.setEvictIdleConnectionsMillis( 60000L );
        }
    }

    protected void applyTimeout( ShuttleTimeoutConfig config ) {
        if ( config == null ) {
            return;
        }
        if ( config.getConnectTimeoutMillis() <= 0L ) {
            config.setConnectTimeoutMillis( 3000L );
        }
        if ( config.getConnectionRequestTimeoutMillis() <= 0L ) {
            config.setConnectionRequestTimeoutMillis( 3000L );
        }
        if ( config.getResponseTimeoutMillis() <= 0L ) {
            config.setResponseTimeoutMillis( 30000L );
        }
    }

    protected void applyHeaderPolicy( ShuttleHeaderPolicyConfig config ) {
        if ( config == null || !config.getBlockedHeaders().isEmpty() ) {
            return;
        }
        config.setBlockedHeaders( List.of(
                "connection",
                "keep-alive",
                "proxy-authenticate",
                "proxy-authorization",
                "te",
                "trailer",
                "transfer-encoding",
                "upgrade"
        ) );
    }

    protected boolean blank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
