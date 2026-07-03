package com.walnut.redstone.ether.shuttle.config;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleRetryConfig implements Pinenut {
    protected boolean enabled;
    protected int maxRetries;
    protected boolean retryIdempotentOnly = true;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public int getMaxRetries() {
        return this.maxRetries;
    }

    public void setMaxRetries( int maxRetries ) {
        this.maxRetries = maxRetries;
    }

    public boolean isRetryIdempotentOnly() {
        return this.retryIdempotentOnly;
    }

    public void setRetryIdempotentOnly( boolean retryIdempotentOnly ) {
        this.retryIdempotentOnly = retryIdempotentOnly;
    }
}
