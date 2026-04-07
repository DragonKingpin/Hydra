package com.walnut.odin.task;


import java.util.Map;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

public class GenericRavenTaskConfig extends ArchKernelObjectConfig implements RavenTaskConfig {

    protected String mszInstanceTitleTimeFormat = RavenTaskConstants.InstanceTitleTimeFormat;
    protected String mszDefaultDateTimeFormat   = RavenTaskConstants.DefaultDateTimeFormat;

    protected int    mnScheduleScanThreadCount  = RavenTaskConstants.ScheduleScanThreadCount;
    protected long   mnScheduleScanIdWindow     = RavenTaskConstants.ScheduleScanIdWindow;

    protected String mszSchedulePartitionName   = "__DEFAULT__";
    protected JSONObject mScheduleGlobalAllocatorConfig;

    public GenericRavenTaskConfig() {
        super();
    }

    public GenericRavenTaskConfig( JSONObject main ) {
        super( main.optJSONObject( "kernelConfig" ) );
        JSONObject config = main.optJSONObject( "kernelConfig" );
        this.mszInstanceTitleTimeFormat = (String) config.getOrDefault("instanceTitleTimeFormat", RavenTaskConstants.InstanceTitleTimeFormat);
        this.mszDefaultDateTimeFormat   = (String) config.getOrDefault("defaultDateTimeFormat", RavenTaskConstants.DefaultDateTimeFormat);

        this.mnScheduleScanThreadCount  = ( (Number) config.getOrDefault("scheduleScanThreadCount", RavenTaskConstants.ScheduleScanThreadCount) ).intValue();
        this.mnScheduleScanIdWindow     = ( (Number) config.getOrDefault("scheduleScanIdWindow", RavenTaskConstants.ScheduleScanIdWindow) ).longValue();

        this.mszSchedulePartitionName   = main.optJSONObject( "scheduler" ).optString( "partitionName", "__DEFAULT__" );
        this.mScheduleGlobalAllocatorConfig = main.optJSONObject( "scheduler" ).optJSONObject( "globalAllocator" );
    }

    @Override
    public String getInstanceTitleTimeFormat() {
        return this.mszInstanceTitleTimeFormat;
    }

    @Override
    public String getDefaultDateTimeFormat() {
        return this.mszDefaultDateTimeFormat;
    }

    @Override
    public int getScheduleScanThreadCount() {
        return this.mnScheduleScanThreadCount;
    }

    @Override
    public long getScheduleScanIdWindow() {
        return this.mnScheduleScanIdWindow;
    }

    @Override
    public JSONObject getScheduleGlobalAllocatorConfig() {
        return this.mScheduleGlobalAllocatorConfig;
    }

    @Override
    public String getSchedulePartitionName() {
        return this.mszSchedulePartitionName;
    }

}
