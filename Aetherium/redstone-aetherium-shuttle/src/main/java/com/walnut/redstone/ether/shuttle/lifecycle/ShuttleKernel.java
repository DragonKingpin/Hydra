package com.walnut.redstone.ether.shuttle.lifecycle;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.walnut.redstone.ether.shuttle.config.ShuttleConfig;
import com.walnut.redstone.ether.shuttle.config.ShuttleConfigDefaults;
import com.walnut.redstone.ether.shuttle.config.ShuttleConfigValidator;
import com.walnut.redstone.ether.shuttle.error.ShuttleErrorCode;
import com.walnut.redstone.ether.shuttle.error.ShuttleException;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleExchange;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleRequest;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleResponse;
import com.walnut.redstone.ether.shuttle.http.HttpClient5ClassicFactory;
import com.walnut.redstone.ether.shuttle.http.HttpClient5ClassicStreamingShuttleExchange;
import com.walnut.redstone.ether.shuttle.http.HttpClient5AsyncShuttleExchange;
import com.walnut.redstone.ether.shuttle.http.HttpClient5AsyncFactory;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;

public class ShuttleKernel implements ShuttleLifecycle {
    public static final String HttpClientTypeApacheHttpClient5Async = "ApacheHttpClient5Async";
    public static final String HttpClientTypeApacheHttpClient5ClassicStreaming = "ApacheHttpClient5ClassicStreaming";

    protected final ShuttleConfig config;
    protected final HttpClient5AsyncFactory httpClientFactory = new HttpClient5AsyncFactory();
    protected final HttpClient5ClassicFactory classicHttpClientFactory = new HttpClient5ClassicFactory();
    protected CloseableHttpAsyncClient httpClient;
    protected CloseableHttpClient classicHttpClient;
    protected ShuttleExchange exchange;
    protected boolean running;

    public ShuttleKernel( ShuttleConfig config ) {
        this.config = new ShuttleConfigDefaults().apply( config );
        new ShuttleConfigValidator().validate( this.config );
    }

    @Override
    public synchronized void start() {
        if ( this.running || !this.config.isEnabled() ) {
            return;
        }
        if ( HttpClientTypeApacheHttpClient5ClassicStreaming.equals( this.config.getHttpClient().getType() ) ) {
            this.classicHttpClient = this.classicHttpClientFactory.create( this.config );
            this.exchange = new HttpClient5ClassicStreamingShuttleExchange( this.config, this.classicHttpClient );
        }
        else {
            this.httpClient = this.httpClientFactory.create( this.config );
            this.httpClient.start();
            this.exchange = new HttpClient5AsyncShuttleExchange( this.config, this.httpClient );
        }
        this.running = true;
    }

    @Override
    public synchronized void stop() {
        if ( !this.running ) {
            return;
        }
        try {
            if ( this.httpClient != null ) {
                this.httpClient.close();
            }
            if ( this.classicHttpClient != null ) {
                this.classicHttpClient.close();
            }
        }
        catch ( IOException ex ) {
            throw new ShuttleException( ShuttleErrorCode.LifecycleError, ex.getMessage(), ex );
        }
        finally {
            this.running = false;
            this.httpClient = null;
            this.classicHttpClient = null;
            this.exchange = null;
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    public ShuttleStatus status() {
        ShuttleStatus ret = new ShuttleStatus();
        ret.setName( this.config.getName() );
        ret.setEnabled( this.config.isEnabled() );
        ret.setRunning( this.running );
        ret.setClientType( this.config.getHttpClient() == null ? null : this.config.getHttpClient().getType() );
        ret.setDefaultTarget( this.config.getDefaultTarget() );
        ret.setTargetCount( this.config.getTargets() == null ? 0 : this.config.getTargets().size() );
        return ret;
    }

    public ShuttleConfig getConfig() {
        return this.config;
    }

    public CloseableHttpAsyncClient getHttpClient() {
        return this.httpClient;
    }

    public CloseableHttpClient getClassicHttpClient() {
        return this.classicHttpClient;
    }

    public CompletableFuture<ShuttleResponse> exchange( ShuttleRequest request ) {
        if ( !this.running || this.exchange == null ) {
            throw new ShuttleException( ShuttleErrorCode.LifecycleError, "Shuttle kernel is not running." );
        }
        return this.exchange.exchange( request );
    }
}
