package com.pinecone.hydra.task.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;

import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface TaskNodeManipulator extends GUIDNameManipulator {
    void insert(TaskElement taskElement);

    void remove(GUID guid);

    GenericTaskElement getTaskNode(GUID guid);
    @Override
    List<GUID> getGuidsByName(String name);
    /*GenericTaskElement getTaskNode(GUID UUID);*/
    void update(GenericTaskElement serviceNode);
    List<GenericTaskElement> fetchServiceNodeByName(String name);

    /*@Override
    List<GUID> getGuidsByName(String name);*/

    @Override
    List<GUID> getGuidsByNameID(String name, GUID guid);

    List<TaskElement> fetchAllTask();

}
