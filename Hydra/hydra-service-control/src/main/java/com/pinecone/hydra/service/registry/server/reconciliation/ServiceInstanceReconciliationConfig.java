package com.pinecone.hydra.service.registry.server.reconciliation;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSONObject;

public class ServiceInstanceReconciliationConfig implements Pinenut {

    public static final int PageSize = 100;
    public static final int MaximumPagesPerSweep = 4;
    public static final int MaximumObservations = 1000;

    protected boolean enable;
    protected boolean exclusiveRegistry;
    protected long scanIntervalMillis = 30000L;
    protected long minimumOnlineAgeMillis = 60000L;
    protected long missingGraceMillis = 30000L;

    public ServiceInstanceReconciliationConfig() {
    }

    public ServiceInstanceReconciliationConfig( JSONObject config ) {
        if ( config == null ) {
            return;
        }
        this.enable = config.optBoolean( "enable", false );
        this.exclusiveRegistry = config.optBoolean( "exclusiveRegistry", false );
        this.scanIntervalMillis = positive( config.optLong( "scanIntervalMillis", this.scanIntervalMillis ), "scanIntervalMillis" );
        this.minimumOnlineAgeMillis = positive( config.optLong( "minimumOnlineAgeMillis", this.minimumOnlineAgeMillis ), "minimumOnlineAgeMillis" );
        this.missingGraceMillis = positive( config.optLong( "missingGraceMillis", this.missingGraceMillis ), "missingGraceMillis" );
        if ( this.enable && !this.exclusiveRegistry ) {
            throw new IllegalArgumentException( "Instance reconciliation requires exclusiveRegistry=true; shared registries need control-plane ownership first." );
        }
    }

    protected static long positive( long value, String name ) {
        if ( value <= 0 || value > 31536000000L ) {
            throw new IllegalArgumentException( "Invalid instance reconciliation " + name + ": " + value );
        }
        return value;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public boolean isExclusiveRegistry() {
        return this.exclusiveRegistry;
    }

    public long getScanIntervalMillis() {
        return this.scanIntervalMillis;
    }

    public long getMinimumOnlineAgeMillis() {
        return this.minimumOnlineAgeMillis;
    }

    public long getMissingGraceMillis() {
        return this.missingGraceMillis;
    }
}
