package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.executum.EventedTaskManager;
import com.pinecone.framework.util.config.Config;
import com.pinecone.framework.util.name.Name;

import java.util.List;

public interface Orchestrator extends EventedTaskManager {
    String  ConfigOrchestrationKey   = "Orchestration"   ;
    String  ConfigServgramScopesKey  = "ServgramScopes"  ;

    void    orchestrate() throws OrchestrateInterruptException ;

    List    preloads( String szName ) ;

    List    preloads( Name name ) ;

    Config  getScheme();
}
