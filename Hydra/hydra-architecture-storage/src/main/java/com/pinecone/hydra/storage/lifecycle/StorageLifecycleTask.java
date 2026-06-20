package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface StorageLifecycleTask extends Pinenut {
    long getEnumId();

    GUID getGuid();

    StorageLifecycleTaskType getTaskType();

    StorageLifecycleTargetType getTargetType();

    GUID getTargetGuid();

    String getTargetName();

    StorageLifecycleOperationMode getOperationMode();

    StorageLifecycleTaskStatus getStatus();

    StorageLifecyclePhase getPhase();

    long getTotalCount();

    long getDoneCount();

    String getLastCursor();

    StorageLifecycleRiskLevel getRiskLevel();

    String getMessage();

    String getErrorMessage();

    String getPlanSnapshot();

    String getResultSnapshot();

    GUID getOperatorGuid();

    String getExtConfig();

    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();
}
