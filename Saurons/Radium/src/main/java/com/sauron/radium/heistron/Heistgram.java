package com.sauron.radium.heistron;

import com.pinecone.hydra.servgram.Servgramium;
import com.pinecone.hydra.system.Hydrarum;
import com.sauron.radium.heistron.orchestration.HeistletOrchestrator;
import com.pinecone.framework.system.executum.ExclusiveProcessum;
import com.pinecone.framework.util.config.JSONConfig;

public interface Heistgram extends ExclusiveProcessum, Servgramium {
    Hydrarum getSystem();

    JSONConfig getTemplateHeistSchemeConfig();

    JSONConfig getLocalHeistsConfigList();

    HeistletOrchestrator getHeistletOrchestrator();

    JSONConfig queryHeistConfig ( String szHeistName );

    JSONConfig getComponentsConfig();

    String searchHeistName( Heistum that ) ;
}
