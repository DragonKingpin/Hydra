package com.walnut.odin.task.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;

public interface TaskProcessorManipulator extends Pinenut {

    TaskProcessorEntity selectByProcessorName( String szProcessorName );

    TaskProcessorEntity selectByGuid( GUID guid );

    TaskProcessorEntity selectMetadataByGuid( GUID guid );

    List<TaskProcessorEntity> selectByClusterName( String clusterName );

    List<TaskProcessorEntity> selectAll();

    int insert( TaskProcessorEntity entity );

    int updateByGuid( TaskProcessorEntity entity );

    int updateQueueCapacity(
            GUID guid,
            int maxCapacity,
            int minCapacity,
            int runtimeCapacity
    );

    int updateDynamicMetadataCache(
            GUID guid,
            String dyMetadataCache
    );

    int deleteByGuid( GUID guid );

    int disable(  GUID guid );

    int enable(  GUID guid );

}
