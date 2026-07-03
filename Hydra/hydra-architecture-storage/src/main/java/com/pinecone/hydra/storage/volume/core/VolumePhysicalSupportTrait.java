package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class VolumePhysicalSupportTrait implements Pinenut {
    protected long          mnEnumId;
    protected GUID          mGuid;
    protected GUID          mPhysicalGuid;
    protected String        mszCode;
    protected String        mszDisplayName;
    protected String        mszVendor;
    protected String        mszModel;
    protected String        mszProtocolVersion;
    protected String        mszRegion;
    protected String        mszZone;
    protected String        mszCredentialRef;
    protected String        mszTraitConfig;
    protected String        mszCapabilitySnapshot;
    protected String        mszProbeStatus;
    protected LocalDateTime mLastProbeTime;
    protected String        mszStatus;
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

    public GUID getPhysicalGuid() {
        return this.mPhysicalGuid;
    }

    public void setPhysicalGuid( GUID physicalGuid ) {
        this.mPhysicalGuid = physicalGuid;
    }

    public String getCode() {
        return this.mszCode;
    }

    public void setCode( String code ) {
        this.mszCode = code;
    }

    public String getDisplayName() {
        return this.mszDisplayName;
    }

    public void setDisplayName( String displayName ) {
        this.mszDisplayName = displayName;
    }

    public String getVendor() {
        return this.mszVendor;
    }

    public void setVendor( String vendor ) {
        this.mszVendor = vendor;
    }

    public String getModel() {
        return this.mszModel;
    }

    public void setModel( String model ) {
        this.mszModel = model;
    }

    public String getProtocolVersion() {
        return this.mszProtocolVersion;
    }

    public void setProtocolVersion( String protocolVersion ) {
        this.mszProtocolVersion = protocolVersion;
    }

    public String getRegion() {
        return this.mszRegion;
    }

    public void setRegion( String region ) {
        this.mszRegion = region;
    }

    public String getZone() {
        return this.mszZone;
    }

    public void setZone( String zone ) {
        this.mszZone = zone;
    }

    public String getCredentialRef() {
        return this.mszCredentialRef;
    }

    public void setCredentialRef( String credentialRef ) {
        this.mszCredentialRef = credentialRef;
    }

    public String getTraitConfig() {
        return this.mszTraitConfig;
    }

    public void setTraitConfig( String traitConfig ) {
        this.mszTraitConfig = traitConfig;
    }

    public String getCapabilitySnapshot() {
        return this.mszCapabilitySnapshot;
    }

    public void setCapabilitySnapshot( String capabilitySnapshot ) {
        this.mszCapabilitySnapshot = capabilitySnapshot;
    }

    public String getProbeStatus() {
        return this.mszProbeStatus;
    }

    public void setProbeStatus( String probeStatus ) {
        this.mszProbeStatus = probeStatus;
    }

    public LocalDateTime getLastProbeTime() {
        return this.mLastProbeTime;
    }

    public void setLastProbeTime( LocalDateTime lastProbeTime ) {
        this.mLastProbeTime = lastProbeTime;
    }

    public String getStatus() {
        return this.mszStatus;
    }

    public void setStatus( String status ) {
        this.mszStatus = status;
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
