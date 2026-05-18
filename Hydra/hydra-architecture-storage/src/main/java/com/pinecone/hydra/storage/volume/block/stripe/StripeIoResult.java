package com.pinecone.hydra.storage.volume.block.stripe;

import com.pinecone.framework.system.prototype.Pinenut;

public final class StripeIoResult implements Pinenut {
    protected final int mnTransferredBytes;

    public StripeIoResult( int transferredBytes ) {
        if ( transferredBytes < 0 ) {
            throw new IllegalArgumentException( "Negative stripe transferred bytes: " + transferredBytes );
        }
        this.mnTransferredBytes = transferredBytes;
    }

    public int getTransferredBytes() {
        return this.mnTransferredBytes;
    }
}
