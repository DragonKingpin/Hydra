package com.walnut.sparta.ucdn.console.ufm;

import java.nio.file.Path;
import java.util.Map;

public class UCFMConfig implements UFMConfig {

    protected int      mnFileFrameSize;

    protected int      mnBatchTransmitMemberThreshold;

    protected String   mszFileCloudDistributeTransmitTopic;

    protected String   mszFileCloudDistributeEventTopic;

    protected String   mszFileServiceTransmitGroup;

    protected String   mszTemporaryFileExtends;

    protected String   mszMajorTemporaryClusterFileDirectory;

    public UCFMConfig ( Map<String, Object > configMap ) {
        this.mnFileFrameSize = (int)configMap.get("fileFrameSize");
        this.mnBatchTransmitMemberThreshold = (int)configMap.get("batchTransmitMemberThreshold");
        this.mszFileCloudDistributeTransmitTopic = (String) configMap.get("fileCloudDistributeTransmitTopic");
        this.mszFileCloudDistributeEventTopic = (String) configMap.get("fileCloudDistributeEventTopic");
        this.mszFileServiceTransmitGroup = (String) configMap.get("fileServiceTransmitGroup");
        this.mszTemporaryFileExtends = (String) configMap.get("temporaryFileExtends");
        this.mszMajorTemporaryClusterFileDirectory = (String) configMap.get("majorTemporaryClusterFileDirectory");
    }

    @Override
    public int getFileFrameSize() {
        return this.mnFileFrameSize;
    }

    @Override
    public String getFileCloudDistributeTransmitTopic() {
        return this.mszFileCloudDistributeTransmitTopic;
    }

    @Override
    public String getFileCloudDistributeEventTopic() {
        return this.mszFileCloudDistributeEventTopic;
    }

    @Override
    public String getFileServiceTransmitGroup() {
        return this.mszFileServiceTransmitGroup;
    }

    @Override
    public String getTemporaryFileExtends() {
        return this.mszTemporaryFileExtends;
    }

    @Override
    public String getMajorTemporaryClusterFileDirectory() {
        return this.mszMajorTemporaryClusterFileDirectory;
    }

    @Override
    public Path formatTemporaryPath( String segName ) {
        return Path.of( this.getMajorTemporaryClusterFileDirectory(), segName + this.getTemporaryFileExtends() );
    }

    @Override
    public int getBatchTransmitMemberThreshold() {
        return this.mnBatchTransmitMemberThreshold;
    }
}
