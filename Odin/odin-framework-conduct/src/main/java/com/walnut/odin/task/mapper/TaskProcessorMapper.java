package com.walnut.odin.task.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.dispatch.entity.GenericTaskProcessorEntity;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.task.source.TaskProcessorManipulator;

@Mapper
@IbatisDataAccessObject
public interface TaskProcessorMapper extends TaskProcessorManipulator {

    GenericTaskProcessorEntity selectByProcessorName( @Param("name") String szProcessorName );


    GenericTaskProcessorEntity selectByGuid( @Param("guid") GUID guid );


    List<GenericTaskProcessorEntity> selectByClusterName0( @Param("clusterName") String clusterName );


    @Override
    @SuppressWarnings("unchecked")
    default List<TaskProcessorEntity> selectByClusterName( @Param("clusterName") String clusterName ) {
        return (List) this.selectByClusterName0( clusterName );
    }


    List<GenericTaskProcessorEntity> selectAll0();


    @Override
    @SuppressWarnings("unchecked")
    default List<TaskProcessorEntity> selectAll() {
        return (List) this.selectAll0();
    }


    int insert( @Param("entity") TaskProcessorEntity entity );


    int updateByGuid( TaskProcessorEntity entity );


    int updateQueueCapacity(
            @Param("guid") GUID guid,
            @Param("maxCapacity") int maxCapacity,
            @Param("minCapacity") int minCapacity,
            @Param("runtimeCapacity") int runtimeCapacity
    );


    int deleteByGuid( @Param("guid") GUID guid );


    int enable( @Param("guid") GUID guid );


    int disable( @Param("guid") GUID guid );

}
