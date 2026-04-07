package com.walnut.odin.task;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.system.ko.KernelObjectConfig;

public interface RavenTaskConfig extends KernelObjectConfig {

    String getInstanceTitleTimeFormat();

    String getDefaultDateTimeFormat();

    int getScheduleScanThreadCount();

    long getScheduleScanIdWindow();

    JSONObject getScheduleGlobalAllocatorConfig();

    String getSchedulePartitionName();

}
