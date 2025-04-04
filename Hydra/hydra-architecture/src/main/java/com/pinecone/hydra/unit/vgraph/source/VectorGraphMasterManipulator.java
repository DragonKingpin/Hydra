package com.pinecone.hydra.unit.vgraph.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;

public interface VectorGraphMasterManipulator extends Pinenut {
    VectorGraphManipulator getVectorGraphManipulator();

    VectorGraphPathCacheManipulator getVectorGraphPathCacheManipulator();
}
