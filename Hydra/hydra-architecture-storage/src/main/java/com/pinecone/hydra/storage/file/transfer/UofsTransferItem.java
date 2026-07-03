package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface UofsTransferItem extends Pinenut {
    long getEnumId();

    GUID getGuid();

    GUID getTaskGuid();

    long getItemIndex();

    int getDepth();

    UofsTransferSourceType getSourceType();

    GUID getSourceBucketGuid();

    GUID getSourceGuid();

    GUID getSourceParentGuid();

    String getSourcePath();

    String getSourcePathHash();

    GUID getTargetBucketGuid();

    GUID getTargetGuid();

    GUID getTargetParentGuid();

    String getTargetPath();

    String getTargetPathHash();

    UofsTransferItemStatus getStatus();

    UofsTransferPhase getPhase();

    long getTotalBytes();

    long getDoneBytes();

    int getRetryCount();

    String getMessage();

    String getErrorMessage();

    String getExtConfig();

    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();
}
