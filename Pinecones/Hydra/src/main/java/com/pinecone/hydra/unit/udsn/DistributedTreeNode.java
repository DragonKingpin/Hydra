package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.UUID;

/**
 *  Pinecone Ursus For Java
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Uniform Distributed Scope Node (UDSN)
 *  *****************************************************************************************
 */
public interface DistributedTreeNode extends Pinenut {
    long getEnumId();
    void setEnumId(long enumId);

    GUID getUUID();
    void setUUID(GUID UUID);

    GUID getParentUUID();
    void setParentUUID(GUID parentUUID);

    GUID getBaseDataUUID();
    void setBaseDataUUID(GUID baseDataUUID);

    GUID getNodeMetadataUUID();
    void setNodeMetadataUUID(GUID nodeMetadataUUID);

    String getType();
    void setType(String type);
}
