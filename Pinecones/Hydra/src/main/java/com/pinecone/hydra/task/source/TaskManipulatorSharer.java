package com.pinecone.hydra.task.source;

import com.pinecone.framework.system.prototype.Pinenut;
import org.springframework.scheduling.config.Task;

public interface TaskManipulatorSharer extends Pinenut {
    TaskNodeMetaManipulator         getTaskNodeMetaManipulator();

    TaskNodeManipulator             getTaskNodeManipulator();

    TaskCommonDataManipulator       getTaskCommonDataManipulator();
}
