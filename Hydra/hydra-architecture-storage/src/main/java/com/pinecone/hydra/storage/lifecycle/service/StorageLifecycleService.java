package com.pinecone.hydra.storage.lifecycle.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.lifecycle.StorageLifecyclePlan;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleRequest;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTask;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTaskStatus;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTaskType;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTargetType;

import java.util.List;
import java.util.Map;

public interface StorageLifecycleService extends Pinenut {
    Map<String, Object> kernelStatus();

    StorageLifecyclePlan plan( StorageLifecycleRequest request );

    StorageLifecycleTask execute( StorageLifecycleRequest request );

    long countTasks(
            StorageLifecycleTaskType taskType,
            StorageLifecycleTargetType targetType,
            StorageLifecycleTaskStatus status,
            GUID targetGuid
    );

    List<? extends StorageLifecycleTask> listTasks(
            StorageLifecycleTaskType taskType,
            StorageLifecycleTargetType targetType,
            StorageLifecycleTaskStatus status,
            GUID targetGuid,
            int offset,
            int limit
    );

    StorageLifecycleTask getTask( GUID taskGuid );

    void cancel( GUID taskGuid );
}
