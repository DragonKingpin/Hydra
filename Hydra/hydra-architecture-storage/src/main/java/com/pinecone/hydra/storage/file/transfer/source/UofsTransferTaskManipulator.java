package com.pinecone.hydra.storage.file.transfer.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.transfer.UofsTransferOperation;
import com.pinecone.hydra.storage.file.transfer.UofsTransferStatus;
import com.pinecone.hydra.storage.file.transfer.UofsTransferTask;

import java.util.List;

public interface UofsTransferTaskManipulator extends Pinenut {
    void insert( UofsTransferTask task );

    void update( UofsTransferTask task );

    UofsTransferTask get( GUID guid );

    long count( UofsTransferOperation operation, UofsTransferStatus status, GUID sourceBucketGuid, GUID targetBucketGuid );

    List<? extends UofsTransferTask> listPage(
            UofsTransferOperation operation,
            UofsTransferStatus status,
            GUID sourceBucketGuid,
            GUID targetBucketGuid,
            int offset,
            int limit
    );

    void updateStatus( GUID guid, String status, String phase, String message, String errorMessage );

    void updateProgress(
            GUID guid,
            String phase,
            long totalCount,
            long doneCount,
            long failedCount,
            long totalBytes,
            long doneBytes,
            GUID lastItemGuid,
            GUID lastSourceGuid,
            GUID lastTargetGuid,
            String message
    );

    void updateDone( GUID guid, String resultSnapshot, String message );

    void delete( GUID guid );
}
