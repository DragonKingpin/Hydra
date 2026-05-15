package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.block.StripedVolume;

import java.time.LocalDateTime;

public class VolumeRecord implements Pinenut {
    protected long          mnEnumId;
    protected GUID          mGuid;
    protected String        mszName;
    protected String        mszVolumeType;
    protected String        mszMappingMode;
    protected String        mszAllocationMode;
    protected String        mszStatus;
    protected long          mnLogicalSize;
    protected long          mnCommittedBytes;
    protected long          mnAllocationUnit;
    protected Long          mnStripeUnit;
    protected Long          mnMemberLength;
    protected Integer       mnMemberCount;
    protected String        mszTitanRoot;
    protected String        mszObjectRoot;
    protected long          mnVersion;
    protected String        mszExtConfig;
    protected LocalDateTime mCreateTime;
    protected LocalDateTime mUpdateTime;

    public long getEnumId() {
        return this.mnEnumId;
    }

    public void setEnumId( long enumId ) {
        this.mnEnumId = enumId;
    }

    public GUID getGuid() {
        return this.mGuid;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    public String getName() {
        return this.mszName;
    }

    public void setName( String name ) {
        this.mszName = name;
    }

    public String getVolumeType() {
        return this.mszVolumeType;
    }

    public void setVolumeType( String volumeType ) {
        this.mszVolumeType = volumeType;
    }

    public String getMappingMode() {
        return this.mszMappingMode;
    }

    public void setMappingMode( String mappingMode ) {
        this.mszMappingMode = mappingMode;
    }

    public String getAllocationMode() {
        return this.mszAllocationMode;
    }

    public void setAllocationMode( String allocationMode ) {
        this.mszAllocationMode = allocationMode;
    }

    public String getStatus() {
        return this.mszStatus;
    }

    public void setStatus( String status ) {
        this.mszStatus = status;
    }

    public long getLogicalSize() {
        return this.mnLogicalSize;
    }

    public void setLogicalSize( long logicalSize ) {
        this.mnLogicalSize = logicalSize;
    }

    public long getCommittedBytes() {
        return this.mnCommittedBytes;
    }

    public void setCommittedBytes( long committedBytes ) {
        this.mnCommittedBytes = committedBytes;
    }

    public long getAllocationUnit() {
        return this.mnAllocationUnit;
    }

    public void setAllocationUnit( long allocationUnit ) {
        this.mnAllocationUnit = allocationUnit;
    }

    public Long getStripeUnit() {
        return this.mnStripeUnit;
    }

    public void setStripeUnit( Long stripeUnit ) {
        this.mnStripeUnit = stripeUnit;
    }

    public Long getMemberLength() {
        return this.mnMemberLength;
    }

    public void setMemberLength( Long memberLength ) {
        this.mnMemberLength = memberLength;
    }

    public Integer getMemberCount() {
        return this.mnMemberCount;
    }

    public void setMemberCount( Integer memberCount ) {
        this.mnMemberCount = memberCount;
    }

    public String getTitanRoot() {
        return this.mszTitanRoot;
    }

    public void setTitanRoot( String titanRoot ) {
        this.mszTitanRoot = titanRoot;
    }

    public String getObjectRoot() {
        return this.mszObjectRoot;
    }

    public void setObjectRoot( String objectRoot ) {
        this.mszObjectRoot = objectRoot;
    }

    public long getVersion() {
        return this.mnVersion;
    }

    public void setVersion( long version ) {
        this.mnVersion = version;
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

    public static VolumeRecord fromVolume( Volume volume ) {
        VolumeRecord record = new VolumeRecord();
        record.setGuid( volume.getGuid() );
        record.setName( volume.getName() );
        record.setVolumeType( volume.getVolumeType().name() );
        record.setMappingMode( volume.getMappingMode().name() );
        record.setAllocationMode( volume.getAllocationMode() == null ? null : volume.getAllocationMode().name() );
        record.setStatus( volume.getStatus().name() );
        record.setLogicalSize( volume.getLogicalSize() );
        record.setCommittedBytes( volume.getCommittedBytes() );
        record.setAllocationUnit( volume.getAllocationUnit() );
        record.setObjectRoot( volume.getObjectRoot() );
        if ( volume instanceof StripedVolume ) {
            StripedVolume stripedVolume = (StripedVolume) volume;
            record.setStripeUnit( stripedVolume.getStripeUnit() );
            record.setMemberLength( stripedVolume.getMemberLength() );
            record.setMemberCount( stripedVolume.getMembers().size() );
        }
        return record;
    }
}

