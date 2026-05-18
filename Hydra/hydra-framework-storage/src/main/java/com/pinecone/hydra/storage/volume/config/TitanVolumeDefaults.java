package com.pinecone.hydra.storage.volume.config;

public final class TitanVolumeDefaults {
    public static final long DefaultAllocationUnit = 64L * 1024L * 1024L;
    public static final long DefaultStripeUnit     = 64L * 1024L;

    private TitanVolumeDefaults() {
    }
}
