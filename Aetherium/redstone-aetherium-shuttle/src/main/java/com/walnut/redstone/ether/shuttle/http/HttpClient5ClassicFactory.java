package com.walnut.redstone.ether.shuttle.http;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.config.ShuttleConfig;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

public class HttpClient5ClassicFactory implements Pinenut {
    public CloseableHttpClient create( ShuttleConfig config ) {
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal( config.getPool().getMaxTotalConnections() )
                .setMaxConnPerRoute( config.getPool().getMaxConnectionsPerRoute() )
                .setConnectionTimeToLive( TimeValue.ofMilliseconds( config.getPool().getConnectionTimeToLiveMillis() ) )
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout( Timeout.ofMilliseconds( config.getTimeout().getConnectTimeoutMillis() ) )
                .setConnectionRequestTimeout( Timeout.ofMilliseconds( config.getTimeout().getConnectionRequestTimeoutMillis() ) )
                .setResponseTimeout( Timeout.ofMilliseconds( config.getTimeout().getResponseTimeoutMillis() ) )
                .build();

        return HttpClients.custom()
                .setConnectionManager( connectionManager )
                .setDefaultRequestConfig( requestConfig )
                .setUserAgent( config.getHttpClient().getUserAgent() )
                .evictIdleConnections( TimeValue.ofMilliseconds( config.getPool().getEvictIdleConnectionsMillis() ) )
                .build();
    }
}
