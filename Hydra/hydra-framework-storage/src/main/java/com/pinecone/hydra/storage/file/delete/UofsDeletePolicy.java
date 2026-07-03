package com.pinecone.hydra.storage.file.delete;

import com.pinecone.framework.system.prototype.Pinenut;

public class UofsDeletePolicy implements Pinenut {
    protected final boolean noopMissing;
    protected final boolean ignoreMissingData;
    protected final boolean ignoreMissingMetadata;
    protected final boolean ignoreMissingNativeTarget;

    protected UofsDeletePolicy(
            boolean noopMissing,
            boolean ignoreMissingData,
            boolean ignoreMissingMetadata,
            boolean ignoreMissingNativeTarget
    ) {
        this.noopMissing = noopMissing;
        this.ignoreMissingData = ignoreMissingData;
        this.ignoreMissingMetadata = ignoreMissingMetadata;
        this.ignoreMissingNativeTarget = ignoreMissingNativeTarget;
    }

    public static UofsDeletePolicy strict() {
        return new UofsDeletePolicy( false, false, false, false );
    }

    public static UofsDeletePolicy idempotent() {
        return new UofsDeletePolicy( true, true, true, true );
    }

    public boolean getNoopMissing() {
        return this.noopMissing;
    }

    public boolean getIgnoreMissingData() {
        return this.ignoreMissingData;
    }

    public boolean getIgnoreMissingMetadata() {
        return this.ignoreMissingMetadata;
    }

    public boolean getIgnoreMissingNativeTarget() {
        return this.ignoreMissingNativeTarget;
    }
}
