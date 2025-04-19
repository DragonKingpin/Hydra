package com.pinecone.hydra.task.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface JobNodeManipulator extends GUIDNameManipulator {

    void insert( JobElement jobElement );

    void remove( GUID guid );

    JobElement getJobElement( GUID guid, TaskInstrument instrument );

    void update( JobElement jobElement );

    List<JobElement> fetchJobNodeByName( String name );

}
