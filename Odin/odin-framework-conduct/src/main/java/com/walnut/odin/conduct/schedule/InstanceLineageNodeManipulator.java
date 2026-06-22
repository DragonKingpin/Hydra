package com.walnut.odin.conduct.schedule;

import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.walnut.odin.conduct.entity.InstanceLineageNode;

public interface InstanceLineageNodeManipulator extends GUIDNameManipulator {

    void insert( InstanceLineageNode instanceLineageNode );
}
