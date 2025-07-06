package com.pinecone.hydra.task.kom;

import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

public class KernelTaskConfig extends ArchKernelObjectConfig implements TaskConfig {

    protected String mszInstanceTitleTimeFormat = TaskMetaConstants.InstanceTitleTimeFormat;

    @Override
    public String getInstanceTitleTimeFormat() {
        return this.mszInstanceTitleTimeFormat;
    }
}
