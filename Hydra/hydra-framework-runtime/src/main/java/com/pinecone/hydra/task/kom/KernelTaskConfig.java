package com.pinecone.hydra.task.kom;

import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

public class KernelTaskConfig extends ArchKernelObjectConfig implements TaskConfig {

    protected String mszBusinessTimeFormat = TaskMetaConstants.BusinessTimeFormat;

    @Override
    public String getBusinessTimeFormat() {
        return this.mszBusinessTimeFormat;
    }
}
