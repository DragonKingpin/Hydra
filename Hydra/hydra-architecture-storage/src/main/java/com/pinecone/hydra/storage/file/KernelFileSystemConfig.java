package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.ArchStorageConfig;
import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.storage.file.cache.DefaultCacheConstants;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.Map;

public class KernelFileSystemConfig extends ArchStorageConfig implements FileSystemConfig {
    protected String mszVersionSignature        = FileConstants.StorageVersionSignature;
    protected Number mnChunkSize                = FileConstants.DefaultChunkSize;
    protected GUID   mLocalhostGUID             = StorageConstants.LocalhostGUID;
    protected Number mTinyFileStripSizing       = FileConstants.TinyFileStripSizing;
    protected long mPathQueryExpiryTimeHotMil   = DefaultCacheConstants.PathQueryExpiryTimeHotMil;
    protected boolean mbJournalEnabled          = FileConstants.DefaultJournalEnabled;
    protected boolean mbJournalAutoRecoveryEnabled = FileConstants.DefaultJournalAutoRecoveryEnabled;
    protected int mnRemoveAsyncThreshold        = 32;

    public KernelFileSystemConfig() {
        super();
    }

    public KernelFileSystemConfig( Map<String, Object> config ) {
        super( config );
        this.mszVersionSignature           = (String) config.getOrDefault("versionSignature", FileConstants.StorageVersionSignature);
        this.mnChunkSize                   = (Number) config.getOrDefault("chunkSize", FileConstants.DefaultChunkSize);
        this.mLocalhostGUID                = GUIDs.GUID128( String.valueOf(config.getOrDefault("localhostGUID", StorageConstants.LocalhostGUID)) );
        this.mTinyFileStripSizing          = (Number) config.getOrDefault("tinyFileStripSizing", FileConstants.TinyFileStripSizing);
        this.mPathQueryExpiryTimeHotMil    = ((Number) config.getOrDefault("pathQueryExpiryTimeHotMil", DefaultCacheConstants.PathQueryExpiryTimeHotMil)).longValue();
        this.mbJournalEnabled              = this.booleanValue( config, "journalEnabled", FileConstants.DefaultJournalEnabled );
        this.mbJournalAutoRecoveryEnabled  = this.booleanValue( config, "journalAutoRecoveryEnabled", FileConstants.DefaultJournalAutoRecoveryEnabled );
        this.mnRemoveAsyncThreshold        = this.removeAsyncThreshold( config );
    }


    @Override
    public String getVersionSignature() {
        return this.mszVersionSignature;
    }

    public Number getChunkSize() {
        return this.mnChunkSize;
    }

    public GUID getLocalhostGUID() {
        return this.mLocalhostGUID;
    }

    @Override
    public Number getmTinyFileStripSizing() {
        return this.mTinyFileStripSizing;
    }

    @Override
    public long getPathQueryExpiryTimeHotMil() {
        return this.mPathQueryExpiryTimeHotMil       ;
    }

    @Override
    public boolean isJournalEnabled() {
        return this.mbJournalEnabled;
    }

    @Override
    public boolean isJournalAutoRecoveryEnabled() {
        return this.mbJournalAutoRecoveryEnabled;
    }

    @Override
    public int getRemoveAsyncThreshold() {
        return this.mnRemoveAsyncThreshold;
    }

    protected int removeAsyncThreshold( Map<String, Object> config ) {
        Object removeConfig = config.get( "remove" );
        if ( removeConfig instanceof Map ) {
            Object value = ( (Map<?, ?>) removeConfig ).get( "asyncThreshold" );
            if ( value instanceof Number ) {
                return Math.max( 1, ( (Number) value ).intValue() );
            }
            if ( value != null ) {
                return Math.max( 1, Integer.parseInt( String.valueOf( value ) ) );
            }
        }
        Object value = config.get( "removeAsyncThreshold" );
        if ( value instanceof Number ) {
            return Math.max( 1, ( (Number) value ).intValue() );
        }
        if ( value != null ) {
            return Math.max( 1, Integer.parseInt( String.valueOf( value ) ) );
        }
        return this.mnRemoveAsyncThreshold;
    }

    protected boolean booleanValue( Map<String, Object> config, String key, boolean defaultValue ) {
        Object value = config.get( key );
        if ( value == null ) {
            return defaultValue;
        }
        if ( value instanceof Boolean ) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean( String.valueOf( value ) );
    }


}
