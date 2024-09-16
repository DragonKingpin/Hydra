package com.pinecone.hydra.system.component;

import com.pinecone.framework.system.construction.InstanceDispenser;
import com.pinecone.hydra.system.HyComponent;

public interface ResourceDispenserCenter extends HyComponent {
    InstanceDispenser getInstanceDispenser() ;
}
