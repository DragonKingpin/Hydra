package com.sauron.radium;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.Pinecore;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.hydra.servgram.*;

public class MasterServgramOrchestrator extends LocalServgramOrchestrator {
    public MasterServgramOrchestrator( Pinecore system, PatriarchalConfig sectionConfig, @Nullable GramFactory factory, GramTransaction transaction ) {
        super( system, sectionConfig, factory, transaction );
    }

    public MasterServgramOrchestrator( Pinecore system, String szSectionName, @Nullable GramFactory factory, GramTransaction transaction ) {
        super( system, system.getGlobalConfig().getChild( szSectionName ), factory, transaction );
    }

    public MasterServgramOrchestrator( Pinecore system, String szSectionName ) {
        super( system, system.getGlobalConfig().getChild( szSectionName ) );
    }

    public MasterServgramOrchestrator( Pinecore system ) {
        this( system, ConfigConstants.KeyMasterOrchestrator );
    }
}
