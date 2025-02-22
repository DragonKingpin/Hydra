package com.pinecone.hydra.account.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.account.entity.Group;

public interface GroupNodeManipulator extends GUIDNameManipulator {
    void insert(Group group);

    void remove(GUID groupGuid);

    Group queryGroup(GUID groupGuid );

    void update(Group group);
}
