package com.walnut.odin.task;


import java.util.Map;

import com.pinecone.framework.system.Nullable;
import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

public class GenericRavenTaskConfig extends ArchKernelObjectConfig implements RavenTaskConfig {

    protected String mszInstanceTitleTimeFormat = RavenTaskConstants.InstanceTitleTimeFormat;

    protected String mszDefaultDateTimeFormat = RavenTaskConstants.DefaultDateTimeFormat;

    public GenericRavenTaskConfig() {
        super();
    }

    public GenericRavenTaskConfig( @Nullable Map<String, Object> config ){
        super( config );
        this.mszInstanceTitleTimeFormat = (String) config.getOrDefault("InstanceTitleTimeFormat", RavenTaskConstants.InstanceTitleTimeFormat);
        this.mszDefaultDateTimeFormat   = (String) config.getOrDefault("DefaultDateTimeFormat", RavenTaskConstants.DefaultDateTimeFormat);
    }

    @Override
    public String getInstanceTitleTimeFormat() {
        return this.mszInstanceTitleTimeFormat;
    }

    @Override
    public String getDefaultDateTimeFormat() {
        return this.mszDefaultDateTimeFormat;
    }
}
