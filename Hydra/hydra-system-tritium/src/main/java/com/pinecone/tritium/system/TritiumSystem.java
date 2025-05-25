package com.pinecone.tritium.system;

import com.pinecone.framework.system.PrimarySystem;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.minister.KernelMicroSystemCabinet;
import com.pinecone.hydra.system.types.HydraKingdom;

public interface TritiumSystem extends HydraKingdom, PrimarySystem {
    InterWareDirector getMiddlewareDirector();

    SystemDaemon getSystemDaemon();

    ServersScope getServersScope() ;

    StorageSystem getStorageSystem() ;

    ConfigScope getPrimaryConfigScope() ;

    ResourceDispenserCenter getDispenserCenter();

    KernelMicroSystemCabinet getKernelMicroSystemCabinet();
}
