package com.pinecone.hydra.storage.file.transfer.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.transfer.UofsTransferItem;
import com.pinecone.hydra.storage.file.transfer.UofsTransferItemStatus;

import java.util.List;

public interface UofsTransferItemManipulator extends Pinenut {
    void insert( UofsTransferItem item );

    void insertBatch( List<? extends UofsTransferItem> items );

    UofsTransferItem get( GUID guid );

    long countByTaskGuid( GUID taskGuid, UofsTransferItemStatus status );

    List<? extends UofsTransferItem> listByTaskGuid( GUID taskGuid, UofsTransferItemStatus status, int offset, int limit );

    List<? extends UofsTransferItem> listRunnable( GUID taskGuid, int limit );

    void updateStatus( GUID guid, String status, String phase, String message, String errorMessage );

    void updateProgress( GUID guid, String phase, long doneBytes, String message );

    void updateDone( GUID guid, GUID targetGuid, String message );

    void increaseRetry( GUID guid );

    void deleteByTaskGuid( GUID taskGuid );
}
