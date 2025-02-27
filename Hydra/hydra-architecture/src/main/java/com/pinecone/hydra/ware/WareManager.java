package com.pinecone.hydra.ware;

import com.pinecone.framework.system.regime.arch.Manager;
import com.pinecone.framework.util.config.Config;

public interface WareManager extends Manager {

    Config getManagedWaresConfig();

    Ware getWare( String name );

}
