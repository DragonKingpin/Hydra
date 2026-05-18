package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.io.PhysicalAccessor;

import java.time.LocalDateTime;

public class VolumeExtent implements Pinenut {
    protected long             mnEnumId;
    protected GUID             mGuid;
    protected GUID             mParentGuid;
    protected GUID             mChildGuid;
    protected GUID             mPhysicalGuid;
    protected Volume           mChildVolume;
    protected PhysicalAccessor mPhysicalAccessor;
    protected VolumeExtentRole mRole;
    protected int              mnOrdinal;
    protected long             mnParentOffset;
    protected long             mnChildOffset;
    protected long             mnPhysicalOffset;
    protected long             mnLength;
    protected String           mszStatus;
    protected String           mszExtConfig;
    protected LocalDateTime    mCreateTime;
    protected LocalDateTime    mUpdateTime;

    public VolumeExtent() {
    }

    public static VolumeExtent forPhysical(
            GUID guid,
            GUID parentGuid,
            PhysicalAccessor physicalAccessor,
            long physicalOffset,
            long length,
            int ordinal,
            VolumeExtentRole role
    ) {
        VolumeExtent extent = new VolumeExtent();
        extent.setGuid( guid );
        extent.setParentGuid( parentGuid );
        extent.setPhysicalAccessor( physicalAccessor );
        extent.setPhysicalGuid( physicalAccessor.getGuid() );
        extent.setPhysicalOffset( physicalOffset );
        extent.setLength( length );
        extent.setOrdinal( ordinal );
        extent.setRole( role );
        return extent;
    }

    public static VolumeExtent forChild(
            GUID guid,
            GUID parentGuid,
            Volume childVolume,
            long childOffset,
            long length,
            int ordinal,
            VolumeExtentRole role
    ) {
        VolumeExtent extent = new VolumeExtent();
        extent.setGuid( guid );
        extent.setParentGuid( parentGuid );
        extent.setChildVolume( childVolume );
        extent.setChildGuid( childVolume.getGuid() );
        extent.setChildOffset( childOffset );
        extent.setLength( length );
        extent.setOrdinal( ordinal );
        extent.setRole( role );
        return extent;
    }

    public GUID getGuid() {
        return this.mGuid;
    }

    public long getEnumId() {
        return this.mnEnumId;
    }

    public void setEnumId( long enumId ) {
        this.mnEnumId = enumId;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    public GUID getParentGuid() {
        return this.mParentGuid;
    }

    public void setParentGuid( GUID parentGuid ) {
        this.mParentGuid = parentGuid;
    }

    public GUID getChildGuid() {
        return this.mChildGuid;
    }

    public void setChildGuid( GUID childGuid ) {
        this.mChildGuid = childGuid;
    }

    public GUID getPhysicalGuid() {
        return this.mPhysicalGuid;
    }

    public void setPhysicalGuid( GUID physicalGuid ) {
        this.mPhysicalGuid = physicalGuid;
    }

    public Volume getChildVolume() {
        return this.mChildVolume;
    }

    public void setChildVolume( Volume childVolume ) {
        this.mChildVolume = childVolume;
    }

    public PhysicalAccessor getPhysicalAccessor() {
        return this.mPhysicalAccessor;
    }

    public void setPhysicalAccessor( PhysicalAccessor physicalAccessor ) {
        this.mPhysicalAccessor = physicalAccessor;
    }

    public VolumeExtentRole getRole() {
        return this.mRole;
    }

    public void setRole( VolumeExtentRole role ) {
        this.mRole = role;
    }

    public int getOrdinal() {
        return this.mnOrdinal;
    }

    public void setOrdinal( int ordinal ) {
        this.mnOrdinal = ordinal;
    }

    public long getParentOffset() {
        return this.mnParentOffset;
    }

    public void setParentOffset( long parentOffset ) {
        this.mnParentOffset = parentOffset;
    }

    public long getChildOffset() {
        return this.mnChildOffset;
    }

    public void setChildOffset( long childOffset ) {
        this.mnChildOffset = childOffset;
    }

    public long getPhysicalOffset() {
        return this.mnPhysicalOffset;
    }

    public void setPhysicalOffset( long physicalOffset ) {
        this.mnPhysicalOffset = physicalOffset;
    }

    public long getLength() {
        return this.mnLength;
    }

    public void setLength( long length ) {
        if ( length < 0 ) {
            throw new IllegalArgumentException( "Negative extent length: " + length );
        }
        this.mnLength = length;
    }

    public String getStatus() {
        return this.mszStatus;
    }

    public void setStatus( String status ) {
        this.mszStatus = status;
    }

    public String getExtConfig() {
        return this.mszExtConfig;
    }

    public void setExtConfig( String extConfig ) {
        this.mszExtConfig = extConfig;
    }

    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    public void setCreateTime( LocalDateTime createTime ) {
        this.mCreateTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    public void setUpdateTime( LocalDateTime updateTime ) {
        this.mUpdateTime = updateTime;
    }
}

