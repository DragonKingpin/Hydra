package com.walnut.sparta.ucdn.console.ufm;

import java.nio.file.Path;
import java.util.Map;

public class UCFMConfig implements UFMConfig {

    protected int      mnFileFrameSize;

    protected int      mnBatchTransmitMemberThreshold;

    protected long     mnSessionExpiredTimeMillis;

    protected String   mszFileCloudDistributeTransmitTopic;

    protected String   mszFileCloudDistributeEventTopic;

    protected String   mszFileServiceTransmitGroup;

    protected String   mszTemporaryFileExtends;

    protected String   mszMajorTemporaryClusterFileDirectory;

    protected String   mszLocalMasterTemporaryClusterFileDirectory;

    public UCFMConfig ( Map<String, Object > configMap ) {
        this.mnFileFrameSize = ( (Number)configMap.get("fileFrameSize") ).intValue();
        this.mnBatchTransmitMemberThreshold = ( (Number)configMap.get("batchTransmitMemberThreshold") ).intValue();
        this.mnSessionExpiredTimeMillis = ( (Number)configMap.get("sessionExpiredTimeMillis") ).longValue();
        this.mszFileCloudDistributeTransmitTopic = (String) configMap.get("fileCloudDistributeTransmitTopic");
        this.mszFileCloudDistributeEventTopic = (String) configMap.get("fileCloudDistributeEventTopic");
        this.mszFileServiceTransmitGroup = (String) configMap.get("fileServiceTransmitGroup");
        this.mszTemporaryFileExtends = (String) configMap.get("temporaryFileExtends");
        this.mszMajorTemporaryClusterFileDirectory = (String) configMap.get("majorTemporaryClusterFileDirectory");
        this.mszLocalMasterTemporaryClusterFileDirectory = (String) configMap.get("localMasterTemporaryClusterFileDirectory");
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
    public long getSessionExpiredTimeMillis() {
        return this.mnSessionExpiredTimeMillis;
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
    public Path formatMasterTemporaryPath( String segName ) {
        return Path.of( this.mszLocalMasterTemporaryClusterFileDirectory, segName + this.getTemporaryFileExtends() );
    }

    @Override
    public int getBatchTransmitMemberThreshold() {
        return this.mnBatchTransmitMemberThreshold;
    }
}
