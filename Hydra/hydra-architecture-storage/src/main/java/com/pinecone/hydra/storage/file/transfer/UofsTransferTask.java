package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface UofsTransferTask extends Pinenut {
    long getEnumId();

    GUID getGuid();

    UofsTransferOperation getOperation();

    int getSourceCount();

    UofsTransferStatus getStatus();

    UofsTransferPhase getPhase();

    String getSourcePath();

    String getSourcePathHash();

    GUID getSourceBucketGuid();

    GUID getSourceGuid();

    UofsTransferSourceType getSourceType();

    String getTargetPath();

    String getTargetPathHash();

    GUID getTargetBucketGuid();

    GUID getTargetParentGuid();

    GUID getTargetGuid();

    UofsTransferConflictPolicy getConflictPolicy();

    UofsTransferLinkPolicy getLinkPolicy();

    long getTotalCount();

    long getDoneCount();

    long getFailedCount();

    long getTotalBytes();

    long getDoneBytes();

    GUID getLastItemGuid();

    GUID getLastSourceGuid();

    GUID getLastTargetGuid();

    String getMessage();

    String getErrorMessage();

    String getPlanSnapshot();

    String getResultSnapshot();

    GUID getOperatorGuid();

    String getExtConfig();

    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();
}
