package com.walnut.odin.task.source;


import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;

public interface RavenTaskMasterManipulator extends KOIMasterManipulator {

    TaskMasterManipulator getTaskMasterManipulator();

    TaskMappingDriver getTaskMappingDriver();

    CategoryTypeManipulator getCategoryTypeManipulator();

    TaskCategoryManipulator getTaskCategoryManipulator();

    CategoryMappingManipulator getCategoryMappingManipulator();

}
