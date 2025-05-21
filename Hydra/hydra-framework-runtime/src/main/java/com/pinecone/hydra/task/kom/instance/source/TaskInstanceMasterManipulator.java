package com.pinecone.hydra.task.kom.instance.source;


import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;

public interface TaskInstanceMasterManipulator extends KOIMasterManipulator {

    TaskMasterManipulator getTaskMasterManipulator();

    InstanceMappingManipulator getInstanceMappingManipulator();

}
