package com.walnut.sailor.stream.fm;

import java.util.Map;

public class SailorFMConfig implements SFMConfig {
    protected String   mszStorageDirectory;

    protected int      mnFileFrameSize;

    protected long     mnSessionExpiredTimeMillis;

    protected String   mszFileCloudDistributeTransmitTopic;

    protected String   mszFileServiceTransmitGroup;

    public SailorFMConfig ( Map<String, Object > configMap ) {
        this.mnFileFrameSize                     = ( (Number)configMap.get("fileFrameSize") ).intValue();
        this.mnSessionExpiredTimeMillis          = ( (Number)configMap.get("sessionExpiredTimeMillis") ).longValue();
        this.mszFileCloudDistributeTransmitTopic = (String) configMap.get("fileCloudDistributeTransmitTopic");
        this.mszFileServiceTransmitGroup         = (String) configMap.get("fileServiceTransmitGroup");
        this.mszStorageDirectory                 = (String) configMap.get("storageDirectory");
    }

    @Override
    public int getFileFrameSize() {
        return this.mnFileFrameSize;
    }

    @Override
    public long getSessionExpiredTimeMillis() {
        return this.mnSessionExpiredTimeMillis;
    }

    @Override
    public String getFileCloudDistributeTransmitTopic() {
        return this.mszFileCloudDistributeTransmitTopic;
    }

    @Override
    public String getStorageDirectory() {
        return this.mszStorageDirectory;
    }

    @Override
    public String getFileServiceTransmitGroup() {
        return this.mszFileServiceTransmitGroup;
    }
}
