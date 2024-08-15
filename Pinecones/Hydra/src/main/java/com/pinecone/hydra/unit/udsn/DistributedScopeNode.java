package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.system.prototype.Pinenut;

/**
 *  Pinecone Ursus For Java
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Uniform Distributed Scope Node (UDSN)
 *  *****************************************************************************************
 */
public interface DistributedScopeNode extends Pinenut {
    String getId();
    void setId(String id);

    String getUUID();
    void setUUID(String UUID);

    String getParentUUID();
    void setParentUUID(String parentUUID);

    String getBaseDataUUID();
    void setBaseDataUUID(String baseDataUUID);

    String getNodeMetadataUUID();
    void setNodeMetadataUUID(String nodeMetadataUUID);

    String getType();
    void setType(String type);
}
