package com.walnut.sparta.uofs.console.infrastructure;

import java.util.Map;

public class UOFSCommonConfig implements UOFSConfig{
    protected String mszPhysicalVolumeType;

    protected String mszSimpleVolumeType;

    protected String mszSpannedVolumeType;

    protected String mszStripedVolumeType;

    protected String mszVersionPrefix;

    public UOFSCommonConfig(){}

    public UOFSCommonConfig(Map<String, Object> config){
        this.mszPhysicalVolumeType = (String) config.get("PhysicalVolumeType");
        this.mszSimpleVolumeType = (String) config.get("SimpleVolumeType");
        this.mszSpannedVolumeType = (String) config.get("SpannedVolumeType");
        this.mszStripedVolumeType = (String) config.get("StripedVolumeType");
        this.mszVersionPrefix = (String) config.get("VersionPrefix");
    }

    @Override
    public String getPhysicalVolumeType() {
        return this.mszPhysicalVolumeType;
    }

    @Override
    public String getSimpleVolumeType() {
        return this.mszSimpleVolumeType;
    }

    @Override
    public String getSpannedVolumeType() {
        return this.mszSpannedVolumeType;
    }

    @Override
    public String getStripedVolumeType() {
        return this.mszStripedVolumeType;
    }

    @Override
    public String getVersionPrefix() {
        return this.mszVersionPrefix;
    }
}
