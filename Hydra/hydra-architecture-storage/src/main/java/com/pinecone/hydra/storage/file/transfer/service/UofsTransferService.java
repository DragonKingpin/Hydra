package com.pinecone.hydra.storage.file.transfer.service;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.transfer.UofsTransferItem;
import com.pinecone.hydra.storage.file.transfer.UofsTransferItemStatus;
import com.pinecone.hydra.storage.file.transfer.UofsTransferOperation;
import com.pinecone.hydra.storage.file.transfer.UofsTransferStatus;
import com.pinecone.hydra.storage.file.transfer.UofsTransferTask;

import java.util.List;

public interface UofsTransferService extends UofsTransferPlanner, UofsTransferExecutor {
    UofsTransferTask getTask( GUID taskGuid );

    long countTasks( UofsTransferOperation operation, UofsTransferStatus status, GUID sourceBucketGuid, GUID targetBucketGuid );

    List<? extends UofsTransferTask> listTasks(
            UofsTransferOperation operation,
            UofsTransferStatus status,
            GUID sourceBucketGuid,
            GUID targetBucketGuid,
            int offset,
            int limit
    );

    List<? extends UofsTransferItem> listItems( GUID taskGuid, UofsTransferItemStatus status, int offset, int limit );

    void cancel( GUID taskGuid );

    void purgeTask( GUID taskGuid );
}
