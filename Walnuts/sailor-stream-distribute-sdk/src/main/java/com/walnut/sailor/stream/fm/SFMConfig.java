package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;

public interface SFMConfig extends Pinenut {

    int getFileFrameSize();

    String getFileCloudDistributeTransmitTopic();

    String getStorageDirectory();

    String getFileServiceTransmitGroup();

}
