package com.pinecone.hydra.storage.lifecycle.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface StorageLifecycleMasterManipulator extends KOIMasterManipulator {
    StorageLifecycleTaskManipulator getTaskManipulator();
}
