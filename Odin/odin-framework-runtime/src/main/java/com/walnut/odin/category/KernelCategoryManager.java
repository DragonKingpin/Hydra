package com.walnut.odin.category;

import com.pinecone.framework.util.id.GUID;

import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.walnut.odin.category.entity.CategoryTag;
import com.walnut.odin.category.entity.KernelCategory;
import com.walnut.odin.category.entity.TaskCategory;

public interface KernelCategoryManager extends KOMInstrument {

    CategoryConfig KernelCategoryConfig = new KernelCategoryConfig();

    void insert(KernelCategory kernelCategory);

    void remove(String kernelCategoryName);

    KernelCategory query(String kernelCategoryName);

    void insertTaskCategory(TaskCategory taskCategory);

    void insertCategoryTag( CategoryTag categoryTag);

    void update(KernelCategory kernelCategory);

}
