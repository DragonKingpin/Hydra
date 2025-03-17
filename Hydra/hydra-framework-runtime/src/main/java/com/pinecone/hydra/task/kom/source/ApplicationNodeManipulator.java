package com.pinecone.hydra.task.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface ApplicationNodeManipulator extends GUIDNameManipulator {
    void insert(JobElement jobElement);

    void remove(GUID guid);

    JobElement getApplicationNode(GUID guid);

    void update(JobElement jobElement);

    List<JobElement> fetchApplicationNodeByName(String name);
}
