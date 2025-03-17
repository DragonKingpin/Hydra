package com.pinecone.hydra.task.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface TaskNodeManipulator extends GUIDNameManipulator {
    //ServiceNode的CRUD
    void insert(GenericTaskElement serviceNode);

    void remove(GUID UUID);

    GenericTaskElement getServiceNode(GUID UUID);

    void update(GenericTaskElement serviceNode);

    List<GenericTaskElement> fetchServiceNodeByName(String name);

    @Override
    List<GUID> getGuidsByName(String name);

    @Override
    List<GUID> getGuidsByNameID(String name, GUID guid);

    List<TaskElement> fetchAllService();
}
