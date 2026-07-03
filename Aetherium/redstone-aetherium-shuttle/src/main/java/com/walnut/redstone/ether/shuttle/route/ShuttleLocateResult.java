package com.walnut.redstone.ether.shuttle.route;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleLocateResult implements Pinenut {
    protected ShuttleRouteType routeType;
    protected String path;
    protected String uri;
    protected String bucket;
    protected String key;
    protected String targetName;
    protected String targetBaseUrl;
    protected String rewrittenPath;
    protected String proxyUrl;
    protected String redirectUrl;
    protected boolean supported = true;
    protected String reason;

    public ShuttleRouteType getRouteType() {
        return this.routeType;
    }

    public void setRouteType( ShuttleRouteType routeType ) {
        this.routeType = routeType;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri( String uri ) {
        this.uri = uri;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket( String bucket ) {
        this.bucket = bucket;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey( String key ) {
        this.key = key;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName( String targetName ) {
        this.targetName = targetName;
    }

    public String getTargetBaseUrl() {
        return this.targetBaseUrl;
    }

    public void setTargetBaseUrl( String targetBaseUrl ) {
        this.targetBaseUrl = targetBaseUrl;
    }

    public String getRewrittenPath() {
        return this.rewrittenPath;
    }

    public void setRewrittenPath( String rewrittenPath ) {
        this.rewrittenPath = rewrittenPath;
    }

    public String getProxyUrl() {
        return this.proxyUrl;
    }

    public void setProxyUrl( String proxyUrl ) {
        this.proxyUrl = proxyUrl;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setRedirectUrl( String redirectUrl ) {
        this.redirectUrl = redirectUrl;
    }

    public boolean isSupported() {
        return this.supported;
    }

    public void setSupported( boolean supported ) {
        this.supported = supported;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason( String reason ) {
        this.reason = reason;
    }
}
