package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;

import java.util.List;

public interface TaskNodeOwnerMapper extends TireOwnerManipulator {
    void insert(GUID subordinateGuid, GUID ownerGuid);

    void remove(GUID subordinateGuid,GUID ownerGuid);

    void removeBySubordinate(GUID subordinateGuid);

    void removeByOwner(GUID OwnerGuid);

    GUID getOwner(GUID subordinateGuid);

    List<GUID> getSubordinates(GUID guid);
}
