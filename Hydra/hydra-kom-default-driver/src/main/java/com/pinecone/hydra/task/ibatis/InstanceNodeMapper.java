package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.CollectionUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.GenericInstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.pinecone.slime.meta.TableIndex64Meta;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
@IbatisDataAccessObject
public interface InstanceNodeMapper extends InstanceNodeManipulator {

    @Override
    void insert( InstanceEntry instance );

    void update( InstanceEntry instance );

    GenericInstanceEntry queryByGuid0( GUID guid );

    @Override
    default InstanceEntry queryByGuid( GUID guid, TaskInstrument instrument ) {
        GenericInstanceEntry entry = this.queryByGuid0( guid );
        if ( entry == null ) {
            return null;
        }
        entry.apply( instrument );
        return entry;
    }



    int countInstance();

    long countInstanceByName( String name );

    List<GenericInstanceEntry> fetchInstances0( @Param("offset") long offset, @Param("pageSize") long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<InstanceEntry> fetchInstances( TaskInstrument instrument, long offset, long pageSize ) {
        List<GenericInstanceEntry> list = this.fetchInstances0( offset, pageSize );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return (List) list;
    }

    List<GenericInstanceEntry> queryByTaskGuid0( @Param("taskGuid") GUID taskGuid, @Param("offset") long offset, @Param("pageSize") long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<InstanceEntry> queryByTaskGuid( TaskInstrument instrument, GUID taskGuid, long offset, long pageSize ) {
        List<GenericInstanceEntry> list = this.queryByTaskGuid0( taskGuid, offset, pageSize );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return (List) list;
    }

    long countInstanceByTaskGuid( GUID taskGuid );

    GenericInstanceEntry findLastExecuted0( @Param("taskGuid") GUID taskGuid, @Param("bizTime") String bizTime );

    @Override
    default InstanceEntry findLastExecuted( GUID taskGuid, TaskInstrument instrument, String bizTime ) {
        GenericInstanceEntry entry = this.findLastExecuted0( taskGuid, bizTime );
        if ( entry == null ) {
            return null;
        }
        entry.apply( instrument );
        return entry;
    }





    @Override
    TableIndex64Meta selectSchedulableIdRange(
            @Param("runStatus") TaskInstanceStatus runStatus, @Param("targetTime") LocalDateTime targetTime,
            @Param( "actuallyPriority" ) @Nullable Short actuallyPriority
    );

    List<GenericInstanceEntry> fetchSchedulableInstances0(
            @Param( "idMin" ) long idMin, @Param( "idMax" ) long idMax,
            @Param( "runStatus" ) TaskInstanceStatus runStatus, @Param( "targetTime" ) LocalDateTime targetTime,
            @Param( "actuallyPriority" ) @Nullable Short actuallyPriority
    );

    @Override
    default List<InstanceEntry> fetchSchedulableInstances(
            TaskInstrument instrument,
            long idMin, long idMax, TaskInstanceStatus runStatus, LocalDateTime targetTime, @Nullable Short actuallyPriority
    ) {
        List<GenericInstanceEntry> list = this.fetchSchedulableInstances0( idMin, idMax, runStatus, targetTime, actuallyPriority );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return CollectionUtils.genericConvert( list );
    }
}