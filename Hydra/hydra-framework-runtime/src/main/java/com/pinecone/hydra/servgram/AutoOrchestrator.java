package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.executum.EventedTaskManager;
import com.pinecone.framework.util.config.Config;
import com.pinecone.framework.util.name.Name;
import com.pinecone.framework.system.regime.Orchestrator;

import java.util.List;

public interface AutoOrchestrator extends EventedTaskManager, Orchestrator {
    String  ConfigOrchestrationKey   = "Orchestration"   ;
    String  ConfigServgramScopesKey  = "ServgramScopes"  ;

    void    orchestrate() throws OrchestrateInterruptException ;

    List    preloads( String szName ) ;

    List    preloads( Name name ) ;

    Config  getScheme();
}
