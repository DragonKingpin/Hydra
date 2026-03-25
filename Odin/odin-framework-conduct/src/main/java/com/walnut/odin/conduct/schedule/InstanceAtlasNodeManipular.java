package com.walnut.odin.conduct.schedule;

import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.walnut.odin.conduct.entity.InstanceAtlasNode;

public interface InstanceAtlasNodeManipular extends GUIDNameManipulator {

    void insert( InstanceAtlasNode instanceAtlasNode);
}
