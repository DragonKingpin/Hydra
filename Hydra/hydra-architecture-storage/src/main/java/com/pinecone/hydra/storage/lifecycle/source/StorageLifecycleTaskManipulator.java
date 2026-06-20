package com.pinecone.hydra.storage.lifecycle.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTask;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTaskStatus;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTaskType;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTargetType;

import java.util.List;

public interface StorageLifecycleTaskManipulator extends Pinenut {
    void insert( StorageLifecycleTask task );

    void update( StorageLifecycleTask task );

    StorageLifecycleTask get( GUID guid );

    long count(
            StorageLifecycleTaskType taskType,
            StorageLifecycleTargetType targetType,
            StorageLifecycleTaskStatus status,
            GUID targetGuid
    );

    List<? extends StorageLifecycleTask> listPage(
            StorageLifecycleTaskType taskType,
            StorageLifecycleTargetType targetType,
            StorageLifecycleTaskStatus status,
            GUID targetGuid,
            int offset,
            int limit
    );

    void updateProgress( GUID guid, String phase, long totalCount, long doneCount, String lastCursor, String message );

    void updateStatus( GUID guid, String status, String phase, String errorMessage, String message );

    void updateDone( GUID guid, String phase, long totalCount, long doneCount, String message, String resultSnapshot );

    void updateBlocked( GUID guid, String phase, String resultSnapshot );
}
