package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.config.TitanVolumeDefaults;

public abstract class ArchVolume implements Volume {
    public static final long DEFAULT_ALLOCATION_UNIT = TitanVolumeDefaults.DefaultAllocationUnit;

    protected GUID         mGuid;
    protected String       mszName;
    protected VolumeType   mVolumeType;
    protected VolumeMappingMode    mMappingMode;
    protected VolumeAllocationMode mAllocationMode;
    protected String       mszObjectRoot;
    protected VolumeStatus mStatus;
    protected long         mnLogicalSize;
    protected long         mnCommittedBytes;
    protected long         mnAllocationUnit;

    protected ArchVolume() {
        this.mStatus = VolumeStatus.CREATING;
        this.mMappingMode = VolumeMappingMode.VOLUME_BLOCK_EXTENT;
        this.mAllocationMode = VolumeAllocationMode.THIN;
        this.mnAllocationUnit = DEFAULT_ALLOCATION_UNIT;
    }

    protected ArchVolume( GUID guid, String name, VolumeType volumeType ) {
        this();
        this.mGuid       = guid;
        this.mszName     = name;
        this.mVolumeType = volumeType;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    public void setName( String name ) {
        this.mszName = name;
    }

    @Override
    public VolumeType getVolumeType() {
        return this.mVolumeType;
    }

    public void setVolumeType( VolumeType volumeType ) {
        this.mVolumeType = volumeType;
    }

    @Override
    public VolumeMappingMode getMappingMode() {
        return this.mMappingMode;
    }

    public void setMappingMode( VolumeMappingMode mappingMode ) {
        this.mMappingMode = mappingMode;
    }

    @Override
    public VolumeAllocationMode getAllocationMode() {
        return this.mAllocationMode;
    }

    public void setAllocationMode( VolumeAllocationMode allocationMode ) {
        this.mAllocationMode = allocationMode;
    }

    @Override
    public String getObjectRoot() {
        return this.mszObjectRoot;
    }

    public void setObjectRoot( String objectRoot ) {
        this.mszObjectRoot = objectRoot;
    }

    @Override
    public VolumeStatus getStatus() {
        return this.mStatus;
    }

    public void setStatus( VolumeStatus status ) {
        this.mStatus = status;
    }

    @Override
    public long getLogicalSize() {
        return this.mnLogicalSize;
    }

    public void setLogicalSize( long logicalSize ) {
        this.mnLogicalSize = logicalSize;
    }

    @Override
    public long getCommittedBytes() {
        return this.mnCommittedBytes;
    }

    public void setCommittedBytes( long committedBytes ) {
        this.mnCommittedBytes = committedBytes;
    }

    @Override
    public long getAllocationUnit() {
        return this.mnAllocationUnit;
    }

    public void setAllocationUnit( long allocationUnit ) {
        this.mnAllocationUnit = allocationUnit <= 0L ? DEFAULT_ALLOCATION_UNIT : allocationUnit;
    }

    protected int trimLength( long position, int requestedLength ) {
        if ( position < 0 ) {
            throw new IllegalArgumentException( "Negative volume position: " + position );
        }
        if ( position >= this.mnLogicalSize ) {
            return 0;
        }
        long availableLength = this.mnLogicalSize - position;
        return (int)Math.min( requestedLength, availableLength );
    }
}

