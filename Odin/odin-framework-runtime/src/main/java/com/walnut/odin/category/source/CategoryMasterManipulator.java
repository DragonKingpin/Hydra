package com.walnut.odin.category.source;


import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface CategoryMasterManipulator extends KOIMasterManipulator {
    KernelCategoryManipulator getKernelCategoryManipulator();

/*    TaskCategoryManipulator getTaskCategoryManipulator();

    CategoryTagManipulator getCategoryTagManipulator();*/
}
