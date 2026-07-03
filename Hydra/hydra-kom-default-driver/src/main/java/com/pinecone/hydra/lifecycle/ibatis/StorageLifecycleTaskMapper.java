package com.pinecone.hydra.lifecycle.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.lifecycle.GenericStorageLifecycleTask;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTask;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTaskStatus;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTaskType;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTargetType;
import com.pinecone.hydra.storage.lifecycle.source.StorageLifecycleTaskManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface StorageLifecycleTaskMapper extends StorageLifecycleTaskManipulator {
    @Override
    void insert( StorageLifecycleTask task );

    @Override
    void update( StorageLifecycleTask task );

    @Override
    GenericStorageLifecycleTask get( @Param("guid") GUID guid );

    @Override
    long count(
            @Param("taskType") StorageLifecycleTaskType taskType,
            @Param("targetType") StorageLifecycleTargetType targetType,
            @Param("status") StorageLifecycleTaskStatus status,
            @Param("targetGuid") GUID targetGuid
    );

    @Override
    List<GenericStorageLifecycleTask> listPage(
            @Param("taskType") StorageLifecycleTaskType taskType,
            @Param("targetType") StorageLifecycleTargetType targetType,
            @Param("status") StorageLifecycleTaskStatus status,
            @Param("targetGuid") GUID targetGuid,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Override
    void updateProgress(
            @Param("guid") GUID guid,
            @Param("phase") String phase,
            @Param("totalCount") long totalCount,
            @Param("doneCount") long doneCount,
            @Param("lastCursor") String lastCursor,
            @Param("message") String message
    );

    @Override
    void updateStatus(
            @Param("guid") GUID guid,
            @Param("status") String status,
            @Param("phase") String phase,
            @Param("errorMessage") String errorMessage,
            @Param("message") String message
    );

    @Override
    void updateDone(
            @Param("guid") GUID guid,
            @Param("phase") String phase,
            @Param("totalCount") long totalCount,
            @Param("doneCount") long doneCount,
            @Param("message") String message,
            @Param("resultSnapshot") String resultSnapshot
    );

    @Override
    void updateBlocked(
            @Param("guid") GUID guid,
            @Param("phase") String phase,
            @Param("resultSnapshot") String resultSnapshot
    );
}
