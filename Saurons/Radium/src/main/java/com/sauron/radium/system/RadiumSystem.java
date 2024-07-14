package com.sauron.radium.system;

import com.pinecone.framework.system.PrimarySystem;
import com.pinecone.hydra.system.component.Log4jTracerScope;
import com.pinecone.hydra.system.types.HydraKingdom;

public interface RadiumSystem extends HydraKingdom, PrimarySystem {
    MiddlewareManager getMiddlewareManager();

    SystemDaemon getSystemDaemon();

    ServersScope getServersScope() ;

    StorageSystem getStorageSystem() ;

    ConfigScope getPrimaryConfigScope() ;

    ResourceDispenserCenter getDispenserCenter();
}
