package com.walnut.odin.task;

import com.pinecone.hydra.system.ko.KernelObjectConfig;

public interface RavenTaskConfig extends KernelObjectConfig {

    String getInstanceTitleTimeFormat();

    String getDefaultDateTimeFormat();

}
