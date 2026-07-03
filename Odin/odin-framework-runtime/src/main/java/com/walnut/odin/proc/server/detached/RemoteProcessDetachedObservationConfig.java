package com.walnut.odin.proc.server.detached;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSONObject;

public class RemoteProcessDetachedObservationConfig implements Pinenut {

    public static final boolean DefaultEnable                      = true;
    public static final long    DefaultGraceMillis                 = 30000L;
    public static final long    DefaultSweepMillis                 = 5000L;
    public static final int     DefaultExpireAsyncThreads          = 2;
    public static final String  DefaultMissingAfterReconnectPolicy = "fail";

    protected boolean mbEnable;
    protected long    mnGraceMillis;
    protected long    mnSweepMillis;
    protected int     mnExpireAsyncThreads;
    protected String  mszMissingAfterReconnectPolicy;

    public RemoteProcessDetachedObservationConfig() {
        this.mbEnable                       = DefaultEnable;
        this.mnGraceMillis                  = DefaultGraceMillis;
        this.mnSweepMillis                  = DefaultSweepMillis;
        this.mnExpireAsyncThreads           = DefaultExpireAsyncThreads;
        this.mszMissingAfterReconnectPolicy = DefaultMissingAfterReconnectPolicy;
    }

    public RemoteProcessDetachedObservationConfig( JSONObject config ) {
        this();
        if ( config == null ) {
            return;
        }

        this.mbEnable                       = config.optBoolean( "enable", this.mbEnable );
        this.mnGraceMillis                  = this.normalizePositiveLong( config.optLong( "graceMillis", this.mnGraceMillis ), DefaultGraceMillis );
        this.mnSweepMillis                  = this.normalizePositiveLong( config.optLong( "sweepMillis", this.mnSweepMillis ), DefaultSweepMillis );
        this.mnExpireAsyncThreads           = this.normalizePositiveInt( config.optInt( "expireAsyncThreads", this.mnExpireAsyncThreads ), DefaultExpireAsyncThreads );
        this.mszMissingAfterReconnectPolicy = config.optString( "missingAfterReconnectPolicy", this.mszMissingAfterReconnectPolicy );
    }

    protected long normalizePositiveLong( long nValue, long nDefault ) {
        if ( nValue > 0L ) {
            return nValue;
        }
        return nDefault;
    }

    protected int normalizePositiveInt( int nValue, int nDefault ) {
        if ( nValue > 0 ) {
            return nValue;
        }
        return nDefault;
    }

    public boolean isEnable() {
        return this.mbEnable;
    }

    public long getGraceMillis() {
        return this.mnGraceMillis;
    }

    public long getSweepMillis() {
        return this.mnSweepMillis;
    }

    public int getExpireAsyncThreads() {
        return this.mnExpireAsyncThreads;
    }

    public String getMissingAfterReconnectPolicy() {
        return this.mszMissingAfterReconnectPolicy;
    }
}
