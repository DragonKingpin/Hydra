package com.pinecone.hydra.unit.udtt.source;

import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;

public interface TreeMasterManipulator extends KOISkeletonMasterManipulator {
    TireOwnerManipulator getTireOwnerManipulator();

    TrieTreeManipulator getTrieTreeManipulator();

    TriePathManipulator getTriePathManipulator();
}
