package com.pinecone.hydra.device.registry.server.detached;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSONObject;

public class DeviceDetachedObservationConfig implements Pinenut {

    public static final boolean DefaultEnable = false;

    public static final long DefaultGraceMillis = 30000L;

    public static final long DefaultSweepMillis = 3000L;

    public static final int DefaultExpireAsyncThreads = 2;

    public static final String DefaultMissingAfterReconnectPolicy = "Offline";

    protected boolean enable;

    protected long graceMillis;

    protected long sweepMillis;

    protected int expireAsyncThreads;

    protected String missingAfterReconnectPolicy;

    public DeviceDetachedObservationConfig() {
        this.enable = DefaultEnable;
        this.graceMillis = DefaultGraceMillis;
        this.sweepMillis = DefaultSweepMillis;
        this.expireAsyncThreads = DefaultExpireAsyncThreads;
        this.missingAfterReconnectPolicy = DefaultMissingAfterReconnectPolicy;
    }

    public DeviceDetachedObservationConfig( JSONObject config ) {
        this();
        if ( config == null ) {
            return;
        }

        this.enable = config.optBoolean( "enable", this.enable );
        this.graceMillis = this.normalizePositiveLong(
                config.optLong( "graceMillis", this.graceMillis ),
                DefaultGraceMillis
        );
        this.sweepMillis = this.normalizePositiveLong(
                config.optLong( "sweepMillis", this.sweepMillis ),
                DefaultSweepMillis
        );
        this.expireAsyncThreads = this.normalizePositiveInt(
                config.optInt( "expireAsyncThreads", this.expireAsyncThreads ),
                DefaultExpireAsyncThreads
        );
        this.missingAfterReconnectPolicy = config.optString(
                "missingAfterReconnectPolicy",
                this.missingAfterReconnectPolicy
        );
    }

    protected long normalizePositiveLong( long value, long defaultValue ) {
        return value > 0L ? value : defaultValue;
    }

    protected int normalizePositiveInt( int value, int defaultValue ) {
        return value > 0 ? value : defaultValue;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public long getGraceMillis() {
        return this.graceMillis;
    }

    public long getSweepMillis() {
        return this.sweepMillis;
    }

    public int getExpireAsyncThreads() {
        return this.expireAsyncThreads;
    }

    public String getMissingAfterReconnectPolicy() {
        return this.missingAfterReconnectPolicy;
    }
}
