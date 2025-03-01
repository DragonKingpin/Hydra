package com.walnut.sailor.stream.fm;

public class SailorFMConfig implements SFMConfig {
    protected String   mszStorageDirectory;

    protected int      mnFileFrameSize;

    protected String   mszFileCloudDistributeTransmitTopic;

    protected String   mszFileServiceTransmitGroup;


    @Override
    public int getFileFrameSize() {
        return this.mnFileFrameSize;
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
