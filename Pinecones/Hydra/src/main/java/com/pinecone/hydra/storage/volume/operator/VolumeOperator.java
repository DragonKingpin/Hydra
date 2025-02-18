package com.pinecone.hydra.storage.volume.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

public interface VolumeOperator extends TreeNodeOperator {
    void removeStorageObject(GUID volumeGuid,GUID storageObjectGuid);

}
