package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.name.Name;

import java.util.List;

public class LocalServgramOrchestrator extends ArchServgramOrchestrator {
    public LocalServgramOrchestrator( Processum parent, PatriarchalConfig sectionConfig, @Nullable GramFactory factory, GramTransaction transaction ) {
        super( parent, sectionConfig, factory, transaction );
    }

    public LocalServgramOrchestrator( Processum parent, PatriarchalConfig sectionConfig ) {
        super( parent, sectionConfig, null, null );

        this.prepareFactory( new LocalGramFactory( this ) );
        this.setTransaction( new LocalGramTransaction( this, parent ) );
    }

    @Override
    protected List<Servgram > popping( String szName ) {
        return ( (GramFactory)this.getClassFactory() ).popping( szName, szName, this.getSystem() );
    }

    @Override
    protected List<Servgram > popping( Name name ) {
        return ( (GramFactory)this.getClassFactory() ).popping( name, name.getName(), this.getSystem() );
    }
}
