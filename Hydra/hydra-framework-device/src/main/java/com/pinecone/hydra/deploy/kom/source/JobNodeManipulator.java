package com.pinecone.hydra.deploy.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.JobElement;

public interface JobNodeManipulator extends GUIDNameManipulator {

    void insert(JobElement jobElement);

    void remove(GUID guid);

    JobElement getJobElement(GUID guid, DeployInstrument instrument);

    void update(JobElement jobElement);

    List<JobElement> fetchJobNodeByName(String name);

}
