package com.peanut.titan.storage.conduct;

import com.peanut.titan.storage.TitanStorageRegiment;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.fat.FatChunkInstrument;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.component.LogStatuses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitanCollectiveStorageRegiment implements TitanStorageRegiment {

    protected final Logger                  mLogger;

    protected final VolumeManager           mVolumeManager;

    protected final KOMFileSystem           mFileSystem;

    public TitanCollectiveStorageRegiment( VolumeManager volumeManager, KOMFileSystem fileSystem ) {
        this.mLogger                        = LoggerFactory.getLogger( "TitanCollectiveStorageRegiment" );
        this.mVolumeManager                 = volumeManager;
        this.mFileSystem                    = fileSystem;
        this.prepare_titan_collective_storage_regiment_subsystem();
    }

    protected void prepare_titan_collective_storage_regiment_subsystem() {
        this.infoLifecycle( "Preparing Titan collective storage regiment.", LogStatuses.StatusStart );
        this.traceWelcomeInfo();
        this.infoLifecycle( "Preparing Titan collective storage regiment.", LogStatuses.StatusDone );
    }

    protected void traceWelcomeInfo() {
        this.mLogger.info( "---------------------------------------------------------------" );
        this.mLogger.info( "Titan Collective Storage Regiment" );
        this.mLogger.info( "Uniform storage domain orchestration over Hydra storage kernel." );
        this.mLogger.info( "---------------------------------------------------------------" );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public VolumeManager volumeManager() {
        return this.mVolumeManager;
    }

    @Override
    public KOMFileSystem fileSystem() {
        return this.mFileSystem;
    }

    @Override
    public FatChunkInstrument fatChunkInstrument() {
        if ( this.mFileSystem == null ) {
            return null;
        }
        return this.mFileSystem.getFatChunkInstrument();
    }

    @Override
    public boolean isVolumeManagerAvailable() {
        return this.mVolumeManager != null;
    }

    @Override
    public boolean isFileSystemAvailable() {
        return this.mFileSystem != null;
    }
}
