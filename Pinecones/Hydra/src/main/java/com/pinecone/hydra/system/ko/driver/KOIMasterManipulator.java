package com.pinecone.hydra.system.ko.driver;

import com.pinecone.framework.system.prototype.Pinenut;

public interface KOIMasterManipulator extends Pinenut {
    KOISkeletonMasterManipulator getSkeletonMasterManipulator();

}