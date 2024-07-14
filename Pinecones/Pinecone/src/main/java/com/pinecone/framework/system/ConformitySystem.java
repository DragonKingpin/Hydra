package com.pinecone.framework.system;

import com.pinecone.framework.util.config.SysConfigson;

public interface ConformitySystem extends RuntimeSystem {
    SysConfigson getGlobalConfig() ;

    SysConfigson  getSystemConfig() ;
}
