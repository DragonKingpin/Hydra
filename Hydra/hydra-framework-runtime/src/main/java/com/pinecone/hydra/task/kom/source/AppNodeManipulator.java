package com.pinecone.hydra.task.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface AppNodeManipulator extends GUIDNameManipulator {

    void insert( AppElement appElement);

    void remove( GUID guid );

    AppElement getAppElement( GUID guid, TaskInstrument instrument );

    void update( AppElement appElement);

    List<AppElement> fetchAppNodeByName( String name );

}
