package com.pinecone.framework.util.config;

import com.pinecone.framework.system.RuntimeSystem;

public interface SystemConfig extends Config {
    RuntimeSystem getSystem();
}
