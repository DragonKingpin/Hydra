package com.walnut.odin.category.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface CategoryTag extends Pinenut {

    void setTaskGuid(GUID taskGuid);

    GUID getTaskGuid();

    void setKernelCategoryName(String kernelCategoryName);

    String getKernelCategoryName();

    void setTaskCategoryName(String TaskCategoryName);

    String getTaskCategoryName();
}
