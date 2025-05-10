package com.pinecone.hydra.deploy.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface TaskNodeManipulator extends GUIDNameManipulator {

    void insert(DeployElement taskElement);

    void remove(GUID UUID);

    DeployElement getTaskNode(GUID guid, DeployInstrument instrument);

    void update(DeployElement taskElement);

    List<DeployElement> fetchTaskNodeByName(String name);

    @Override
    List<GUID> getGuidsByName(String name);

    @Override
    List<GUID> getGuidsByNameID(String name, GUID guid);


}
