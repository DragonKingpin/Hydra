package com.walnut.redstone.ether.shuttle.config;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleConfig implements Pinenut {
    protected String name;
    protected boolean enabled = true;
    protected String defaultTarget;
    protected ShuttleHttpClientConfig httpClient = new ShuttleHttpClientConfig();
    protected ShuttlePoolConfig pool = new ShuttlePoolConfig();
    protected ShuttleTimeoutConfig timeout = new ShuttleTimeoutConfig();
    protected ShuttleRetryConfig retry = new ShuttleRetryConfig();
    protected ShuttleHeaderPolicyConfig headerPolicy = new ShuttleHeaderPolicyConfig();
    protected ShuttleProxyConfig proxy = new ShuttleProxyConfig();
    protected ShuttleTlsConfig tls = new ShuttleTlsConfig();
    protected List<ShuttleTargetConfig> targets = new ArrayList<>();

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public String getDefaultTarget() {
        return this.defaultTarget;
    }

    public void setDefaultTarget( String defaultTarget ) {
        this.defaultTarget = defaultTarget;
    }

    public ShuttleHttpClientConfig getHttpClient() {
        return this.httpClient;
    }

    public void setHttpClient( ShuttleHttpClientConfig httpClient ) {
        this.httpClient = httpClient;
    }

    public ShuttlePoolConfig getPool() {
        return this.pool;
    }

    public void setPool( ShuttlePoolConfig pool ) {
        this.pool = pool;
    }

    public ShuttleTimeoutConfig getTimeout() {
        return this.timeout;
    }

    public void setTimeout( ShuttleTimeoutConfig timeout ) {
        this.timeout = timeout;
    }

    public ShuttleRetryConfig getRetry() {
        return this.retry;
    }

    public void setRetry( ShuttleRetryConfig retry ) {
        this.retry = retry;
    }

    public ShuttleHeaderPolicyConfig getHeaderPolicy() {
        return this.headerPolicy;
    }

    public void setHeaderPolicy( ShuttleHeaderPolicyConfig headerPolicy ) {
        this.headerPolicy = headerPolicy;
    }

    public ShuttleProxyConfig getProxy() {
        return this.proxy;
    }

    public void setProxy( ShuttleProxyConfig proxy ) {
        this.proxy = proxy;
    }

    public ShuttleTlsConfig getTls() {
        return this.tls;
    }

    public void setTls( ShuttleTlsConfig tls ) {
        this.tls = tls;
    }

    public List<ShuttleTargetConfig> getTargets() {
        return this.targets;
    }

    public void setTargets( List<ShuttleTargetConfig> targets ) {
        this.targets = targets == null ? new ArrayList<>() : new ArrayList<>( targets );
    }
}
