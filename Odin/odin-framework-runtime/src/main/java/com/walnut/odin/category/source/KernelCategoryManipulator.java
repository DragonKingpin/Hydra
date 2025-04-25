package com.walnut.odin.category.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.category.entity.KernelCategory;


public interface KernelCategoryManipulator extends Pinenut {

    void insert(KernelCategory kernelCategory);

    KernelCategory queryKernelCategory(String kernelCategoryName);

    void remove(String kernelCategoryName);

    void update(KernelCategory kernelCategory);
}
