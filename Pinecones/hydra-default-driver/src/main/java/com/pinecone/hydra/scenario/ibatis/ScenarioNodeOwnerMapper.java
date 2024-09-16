package com.pinecone.hydra.scenario.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ScenarioNodeOwnerMapper extends TireOwnerManipulator {

    void insert(GUID subordinateGuid, GUID ownerGuid);

    void remove(GUID subordinateGuid,GUID ownerGuid);

    void removeBySubordinate(GUID subordinateGuid);

    void removeByOwner(GUID OwnerGuid);

    GUID getOwner(GUID subordinateGuid);

    List<GUID> getSubordinates(GUID guid);
}
