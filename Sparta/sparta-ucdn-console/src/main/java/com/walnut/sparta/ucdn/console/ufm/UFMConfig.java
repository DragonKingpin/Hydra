package com.walnut.sparta.ucdn.console.ufm;

import java.nio.file.Path;

import com.pinecone.framework.system.prototype.Pinenut;

public interface UFMConfig extends Pinenut {

    int getFileFrameSize();

    String getFileCloudDistributeTransmitTopic();

    String getFileCloudDistributeEventTopic();

    String getFileServiceTransmitGroup();

    String getTemporaryFileExtends();

    String getMajorTemporaryClusterFileDirectory();

    Path formatTemporaryPath( String segName );

    long getSessionExpiredTimeMillis();

    int getBatchTransmitMemberThreshold();

}
