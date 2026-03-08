package com.walnut.odin.task.source;


import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;

public interface RavenTaskMasterManipulator extends KOIMasterManipulator {

    TaskMasterManipulator getTaskMasterManipulator();

    KOIMappingDriver getTaskMappingDriver();

    CategoryTypeManipulator getCategoryTypeManipulator();

    TaskCategoryManipulator getTaskCategoryManipulator();

    CategoryMappingManipulator getCategoryMappingManipulator();

    TaskExMetaManipulator getTaskExMetaManipulator();

    TaskProcessorManipulator getTaskProcessorManipulator();

}
