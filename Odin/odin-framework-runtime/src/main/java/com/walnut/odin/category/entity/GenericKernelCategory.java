package com.walnut.odin.category.entity;

import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;


public class GenericKernelCategory implements KernelCategory {
    protected String kernelCategoryName;

    protected String kernelCategoryNickName;

    protected String kernelCategoryDescription;

    @Override
    public void setKernelCategoryName(String kernelCategoryName) {
        this.kernelCategoryName = kernelCategoryName;
    }

    @Override
    public String getKernelCategoryName() {
        return this.kernelCategoryName;
    }

    @Override
    public void setKernelCategoryNickName(String kernelCategoryNick) {
        this.kernelCategoryNickName = kernelCategoryNick;
    }

    @Override
    public String getKernelCategoryNickName() {
        return this.kernelCategoryNickName;
    }

    @Override
    public void setKernelCategoryDescription(String kernelCategoryDescription) {
        this.kernelCategoryDescription = kernelCategoryDescription;
    }

    @Override
    public String getKernelCategoryDescription() {
        return this.kernelCategoryDescription;
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
