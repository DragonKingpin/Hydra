package com.pinecone.ulf.beans.construction;

import org.springframework.context.ConfigurableApplicationContext;

import com.pinecone.framework.system.construction.InstanceManufacturer;

public interface UlfInstanceManufacturer extends InstanceManufacturer {
    ConfigurableApplicationContext getApplicationContext();
}
