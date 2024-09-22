package com.pinecone.hydra.task.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface TaskMasterManipulator extends KOIMasterManipulator {
    TaskNodeMetaManipulator         getTaskNodeMetaManipulator();

    TaskNodeManipulator             getTaskNodeManipulator();

    TaskCommonDataManipulator       getTaskCommonDataManipulator();
}
