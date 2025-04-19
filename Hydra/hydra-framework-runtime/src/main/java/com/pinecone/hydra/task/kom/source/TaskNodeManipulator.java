package com.pinecone.hydra.task.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface TaskNodeManipulator extends GUIDNameManipulator {
    //ServiceNode的CRUD
    void insert( TaskElement serviceNode );

    void remove( GUID UUID );

    TaskElement getTaskNode( GUID guid, TaskInstrument instrument );

    void update( TaskElement serviceNode );

    List<TaskElement> fetchTaskNodeByName( String name );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );


}
