package com.walnut.odin.category.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public interface KernelCategory extends Pinenut {
/*     void setKernelCategoryId(long enumId);
     int getKernelCategoryId();*/
     void setKernelCategoryName(String kernelCategoryName);

     String getKernelCategoryName();

     void setKernelCategoryNickName(String kernelCategoryNick);

     String getKernelCategoryNickName();

     void setKernelCategoryDescription(String kernelCategoryDescription);

     String getKernelCategoryDescription();
}
