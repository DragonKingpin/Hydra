package com.walnut.redstone.ether.shuttle.config;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleProxyConfig implements Pinenut {
    protected boolean enabled;
    protected String host;
    protected int port;
    protected String username;
    protected String password;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost( String host ) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort( int port ) {
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }
}
