package com.walnut.redstone.ether.shuttle.http;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.config.ShuttleConfig;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

public class HttpClient5AsyncFactory implements Pinenut {
    public CloseableHttpAsyncClient create( ShuttleConfig config ) {
        PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnTotal( config.getPool().getMaxTotalConnections() )
                .setMaxConnPerRoute( config.getPool().getMaxConnectionsPerRoute() )
                .setConnectionTimeToLive( TimeValue.ofMilliseconds( config.getPool().getConnectionTimeToLiveMillis() ) )
                .build();

        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount( config.getHttpClient().getIoThreadCount() )
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout( Timeout.ofMilliseconds( config.getTimeout().getConnectTimeoutMillis() ) )
                .setConnectionRequestTimeout( Timeout.ofMilliseconds( config.getTimeout().getConnectionRequestTimeoutMillis() ) )
                .setResponseTimeout( Timeout.ofMilliseconds( config.getTimeout().getResponseTimeoutMillis() ) )
                .build();

        return HttpAsyncClients.custom()
                .setConnectionManager( connectionManager )
                .setIOReactorConfig( ioReactorConfig )
                .setDefaultRequestConfig( requestConfig )
                .setUserAgent( config.getHttpClient().getUserAgent() )
                .evictIdleConnections( TimeValue.ofMilliseconds( config.getPool().getEvictIdleConnectionsMillis() ) )
                .build();
    }
}
