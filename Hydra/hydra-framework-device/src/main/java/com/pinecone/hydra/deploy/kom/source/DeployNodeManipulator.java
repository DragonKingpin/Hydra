package com.pinecone.hydra.deploy.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface DeployNodeManipulator extends GUIDNameManipulator {

    void insert(DeployElement deployElement);

    void remove(GUID UUID);

    void update(DeployElement taskElement);

    List<DeployElement> fetchDeployNodeByName(String name);

    @Override
    List<GUID> getGuidsByName(String name);

    @Override
    List<GUID> getGuidsByNameID(String name, GUID guid);


}
