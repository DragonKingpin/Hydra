package com.pinecone.hydra.task.kom.source;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.digest.TaskElementDigest;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.slime.meta.TableIndex64Meta;

public interface TaskNodeManipulator extends GUIDNameManipulator {

    void insert( TaskElement taskElement );

    void remove( GUID UUID );

    TaskElement getTaskNode( GUID guid, TaskInstrument instrument );

    void update( TaskElement taskElement );

    List<TaskElement> fetchTaskNodeByName( String name );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );


    TableIndex64Meta selectSchedulableIdRange( Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime );

    List<TaskElement> fetchSchedulableTasksInRange( long idMin, long idMax, Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime );

    List<TaskElement> listPage(int offset, int pageSize);

    List<TaskElementDigest> listDigests( int offset, int pageSize );

    List<TaskElementDigest> fetchDigestsByGuids( Collection<GUID> guids );

}
