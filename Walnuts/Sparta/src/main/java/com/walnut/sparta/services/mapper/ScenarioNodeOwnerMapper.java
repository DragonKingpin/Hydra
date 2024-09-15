package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ScenarioNodeOwnerMapper extends ScopeOwnerManipulator {

    void insert(GUID subordinateGuid, GUID ownerGuid);

    void remove(GUID subordinateGuid,GUID ownerGuid);

    void removeBySubordinate(GUID subordinateGuid);

    void removeByOwner(GUID OwnerGuid);

    GUID getOwner(GUID subordinateGuid);

    List<GUID> getSubordinates(GUID guid);
}
