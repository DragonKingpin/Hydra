package com.walnut.redstone.ether.shuttle.config;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttlePoolConfig implements Pinenut {
    protected int maxTotalConnections;
    protected int maxConnectionsPerRoute;
    protected long connectionTimeToLiveMillis;
    protected long evictIdleConnectionsMillis;

    public int getMaxTotalConnections() {
        return this.maxTotalConnections;
    }

    public void setMaxTotalConnections( int maxTotalConnections ) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getMaxConnectionsPerRoute() {
        return this.maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute( int maxConnectionsPerRoute ) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public long getConnectionTimeToLiveMillis() {
        return this.connectionTimeToLiveMillis;
    }

    public void setConnectionTimeToLiveMillis( long connectionTimeToLiveMillis ) {
        this.connectionTimeToLiveMillis = connectionTimeToLiveMillis;
    }

    public long getEvictIdleConnectionsMillis() {
        return this.evictIdleConnectionsMillis;
    }

    public void setEvictIdleConnectionsMillis( long evictIdleConnectionsMillis ) {
        this.evictIdleConnectionsMillis = evictIdleConnectionsMillis;
    }
}
