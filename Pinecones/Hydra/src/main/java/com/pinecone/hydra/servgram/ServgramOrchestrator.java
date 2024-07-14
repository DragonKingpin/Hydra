package com.pinecone.hydra.servgram;

import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.ulf.util.lang.MultiScopeFactory;
import org.slf4j.Logger;

public interface ServgramOrchestrator extends Orchestrator {
    PatriarchalConfig getSectionConfig();

    PatriarchalConfig getOrchestrationConfig();

    GramTransaction   getTransaction();

    void    setTransaction( GramTransaction transaction );

    void    onlyOrchestrateTransaction() throws OrchestrateInterruptException ;

    void    startTransaction() throws OrchestrateInterruptException ;

    Logger  tracer();

    MultiScopeFactory getClassFactory();
}
