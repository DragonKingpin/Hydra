package com.walnut.odin.category.source;


import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.category.entity.TaskCategory;

public interface TaskCategoryManipulator extends Pinenut {
    void insert(TaskCategory taskCategory);

    TaskCategory queryTaskCategory(String taskCategoryName);
}
