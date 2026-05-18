package com.pinecone.hydra.storage;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.Map;

public abstract class ArchStorageConfig extends ArchKernelObjectConfig implements StorageConfig {
    protected String mszLocalHostGuid = StorageConstants.LocalhostGUID.toString();

    protected String mszDefaultVolumeGuid   ;

    protected String mszDefaultTempFilePath ;
    protected ArchStorageConfig(){
        super();
    }

    public ArchStorageConfig( Map<String, Object> config ){
        super( config );
        this.mszLocalHostGuid       = this.stringValue( config, "LocalHostGuid", "localHostGuid", StorageConstants.LocalhostGUID.toString() );
        this.mszDefaultVolumeGuid   = this.stringValue( config, "DefaultVolumeGuid", "defaultVolumeGuid", null );
        this.mszDefaultTempFilePath = this.stringValue( config, "DefaultTempFilePath", "defaultTempFilePath", null );
    }

    protected String stringValue( Map<String, Object> config, String primaryKey, String secondaryKey, String defaultValue ) {
        Object value = config.get( primaryKey );
        if ( value == null ) {
            value = config.get( secondaryKey );
        }
        if ( value == null ) {
            return defaultValue;
        }
        return String.valueOf( value );
    }

    @Override
    public GUID getLocalHostGuid() {
        return GUIDs.GUID128(this.mszLocalHostGuid);
    }

    @Override
    public String getDefaultVolumeGuid() {
        return this.mszDefaultVolumeGuid;
    }

    @Override
    public String getDefaultTempFilePath() {
        return this.mszDefaultTempFilePath;
    }
}
