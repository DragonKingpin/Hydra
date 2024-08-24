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

    GUID getGuid();
    void setGuid(GUID guid);

    GUID getParentGUID();
    void setParentGUID(GUID parentGUID);

    GUID getBaseDataGUID();
    void setBaseDataGUID(GUID baseDataGUID);

    GUID getNodeMetadataGUID();
    void setNodeMetadataGUID(GUID nodeMetadataGUID);

    String getType();
    void setType(String type);
}
