package com.walnut.redstone.ether.shuttle.client.uri;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.client.RedShuttleClientConfig;
import com.walnut.redstone.ether.shuttle.client.error.RedShuttleUriException;

public class RedShuttleEndpointResolver implements Pinenut {
    protected final RedShuttleClientConfig config;

    public RedShuttleEndpointResolver( RedShuttleClientConfig config ) {
        this.config = config;
    }

    public String resolveEndpoint( RedShuttleUri uri ) {
        if ( uri != null && !this.blank( uri.getEndpoint() ) ) {
            return uri.getEndpoint();
        }
        if ( this.config != null && !this.blank( this.config.getDefaultEndpoint() ) ) {
            return this.config.getDefaultEndpoint();
        }
        throw new RedShuttleUriException( "Red shuttle endpoint is not configured." );
    }

    public String getSystemBucket() {
        if ( this.config == null || this.blank( this.config.getSystemBucket() ) ) {
            return RedShuttleClientConfig.DefaultSystemBucket;
        }
        return this.config.getSystemBucket();
    }

    protected boolean blank( String szValue ) {
        return szValue == null || szValue.trim().isEmpty();
    }
}
