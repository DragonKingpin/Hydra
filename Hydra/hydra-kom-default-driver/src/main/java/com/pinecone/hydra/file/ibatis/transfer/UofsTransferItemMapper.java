package com.pinecone.hydra.file.ibatis.transfer;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.transfer.GenericUofsTransferItem;
import com.pinecone.hydra.storage.file.transfer.UofsTransferItem;
import com.pinecone.hydra.storage.file.transfer.UofsTransferItemStatus;
import com.pinecone.hydra.storage.file.transfer.source.UofsTransferItemManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface UofsTransferItemMapper extends UofsTransferItemManipulator {
    @Override
    void insert( UofsTransferItem item );

    @Override
    void insertBatch( @Param("items") List<? extends UofsTransferItem> items );

    @Override
    GenericUofsTransferItem get( @Param("guid") GUID guid );

    @Override
    long countByTaskGuid( @Param("taskGuid") GUID taskGuid, @Param("status") UofsTransferItemStatus status );

    @Override
    List<GenericUofsTransferItem> listByTaskGuid(
            @Param("taskGuid") GUID taskGuid,
            @Param("status") UofsTransferItemStatus status,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Override
    List<GenericUofsTransferItem> listRunnable( @Param("taskGuid") GUID taskGuid, @Param("limit") int limit );

    @Override
    void updateStatus(
            @Param("guid") GUID guid,
            @Param("status") String status,
            @Param("phase") String phase,
            @Param("message") String message,
            @Param("errorMessage") String errorMessage
    );

    @Override
    void updateProgress(
            @Param("guid") GUID guid,
            @Param("phase") String phase,
            @Param("doneBytes") long doneBytes,
            @Param("message") String message
    );

    @Override
    void updateDone( @Param("guid") GUID guid, @Param("targetGuid") GUID targetGuid, @Param("message") String message );

    @Override
    void increaseRetry( @Param("guid") GUID guid );

    @Override
    void deleteByTaskGuid( @Param("taskGuid") GUID taskGuid );
}
