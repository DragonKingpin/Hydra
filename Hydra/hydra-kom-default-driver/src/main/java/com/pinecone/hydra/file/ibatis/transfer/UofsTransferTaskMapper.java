package com.pinecone.hydra.file.ibatis.transfer;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.transfer.GenericUofsTransferTask;
import com.pinecone.hydra.storage.file.transfer.UofsTransferOperation;
import com.pinecone.hydra.storage.file.transfer.UofsTransferStatus;
import com.pinecone.hydra.storage.file.transfer.UofsTransferTask;
import com.pinecone.hydra.storage.file.transfer.source.UofsTransferTaskManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface UofsTransferTaskMapper extends UofsTransferTaskManipulator {
    @Override
    void insert( UofsTransferTask task );

    @Override
    void update( UofsTransferTask task );

    @Override
    GenericUofsTransferTask get( @Param("guid") GUID guid );

    @Override
    long count(
            @Param("operation") UofsTransferOperation operation,
            @Param("status") UofsTransferStatus status,
            @Param("sourceBucketGuid") GUID sourceBucketGuid,
            @Param("targetBucketGuid") GUID targetBucketGuid
    );

    @Override
    List<GenericUofsTransferTask> listPage(
            @Param("operation") UofsTransferOperation operation,
            @Param("status") UofsTransferStatus status,
            @Param("sourceBucketGuid") GUID sourceBucketGuid,
            @Param("targetBucketGuid") GUID targetBucketGuid,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

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
            @Param("totalCount") long totalCount,
            @Param("doneCount") long doneCount,
            @Param("failedCount") long failedCount,
            @Param("totalBytes") long totalBytes,
            @Param("doneBytes") long doneBytes,
            @Param("lastItemGuid") GUID lastItemGuid,
            @Param("lastSourceGuid") GUID lastSourceGuid,
            @Param("lastTargetGuid") GUID lastTargetGuid,
            @Param("message") String message
    );

    @Override
    void updateDone(
            @Param("guid") GUID guid,
            @Param("resultSnapshot") String resultSnapshot,
            @Param("message") String message
    );

    @Override
    void delete( @Param("guid") GUID guid );
}
