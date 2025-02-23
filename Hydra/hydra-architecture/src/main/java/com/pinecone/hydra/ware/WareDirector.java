package com.pinecone.hydra.ware;

import com.pinecone.framework.system.regime.arch.Director;
import com.pinecone.framework.util.config.Config;

public interface WareDirector extends Director {

    Config getSectionConfig();

    WareManager getManager( String name );

}
