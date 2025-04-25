package com.walnut.odin.category.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;


public class GenericCategoryTag implements CategoryTag {
    GUID taskGuid;
    String kernelCategoryName;

    String taskCategoryName;
    @Override
    public void setTaskGuid(GUID taskGuid) {
        this.taskGuid = taskGuid;
    }

    @Override
    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    @Override
    public void setKernelCategoryName(String kernelCategoryName) {
        this.kernelCategoryName = kernelCategoryName;
    }

    @Override
    public String getKernelCategoryName() {
        return this.kernelCategoryName;
    }

    @Override
    public void setTaskCategoryName(String TaskCategoryName) {
        this.taskCategoryName = TaskCategoryName;
    }

    @Override
    public String getTaskCategoryName() {
        return this.taskCategoryName;
    }
    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
