package com.sauron.radium.heistron.orchestration;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.name.FixScopeName;
import com.pinecone.framework.util.name.Name;
import com.pinecone.hydra.servgram.ArchServgramOrchestrator;
import com.pinecone.hydra.servgram.GramFactory;
import com.pinecone.hydra.servgram.GramTransaction;
import com.pinecone.hydra.servgram.LocalGramTransaction;
import com.pinecone.hydra.servgram.Servgram;
import com.sauron.radium.heistron.Heistgram;

import java.util.ArrayList;
import java.util.List;

public class LocalHeistumOrchestrator extends ArchServgramOrchestrator implements HeistletOrchestrator {
    public final static String     ConfigDirectlyLoadKey = "DirectlyLoad";

    protected Heistgram       mHeistgram;
    protected List            mPreloadPrefixes;
    protected List            mPreloadSuffixes;

    public LocalHeistumOrchestrator( Processum parent, PatriarchalConfig sectionConfig, @Nullable GramFactory factory, GramTransaction transaction ) {
        super( parent, sectionConfig, factory, transaction );

        this.initDirectlyLoad();
    }

    public LocalHeistumOrchestrator( Processum parent, PatriarchalConfig sectionConfig ) {
        this( parent, sectionConfig, null, null );

        this.prepareFactory( new LocalHeistletFactory( this ) );
        this.setTransaction( new LocalGramTransaction( this, parent ) );

        if( parent instanceof Heistgram ) {
            this.mHeistgram = (Heistgram)parent;
        }
    }

    @Override
    public List getPreloadPrefixes() {
        return this.mPreloadPrefixes;
    }

    @Override
    public List getPreloadSuffixes() {
        return this.mPreloadSuffixes;
    }

    @SuppressWarnings( "unchecked" )
    protected void initDirectlyLoad() {
        try{
            PatriarchalConfig jDirectlyLoad = this.getOrchestrationConfig().getChild( LocalHeistumOrchestrator.ConfigDirectlyLoadKey );
            if( jDirectlyLoad != null ) {
                this.mPreloadPrefixes = (List) jDirectlyLoad.getOrDefault( "Prefix", new ArrayList<>() );
                this.mPreloadSuffixes = (List) jDirectlyLoad.getOrDefault( "Suffix", new ArrayList<>() );
            }
            else {
                this.mPreloadPrefixes = new ArrayList<>();
                this.mPreloadSuffixes = new ArrayList<>();
            }

            this.mPreloadPrefixes.add( "" );
            this.mPreloadSuffixes.add( "" );
        }
        catch ( Exception e ) {
            e.printStackTrace( this.getHeistgram().getSystem().console().getErr() );
        }
    }

    @Override
    public Heistgram getHeistgram() {
        return this.mHeistgram;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    protected List<Servgram > popping( String szName ) {
        List<String > prefixes = new ArrayList<>( this.mPreloadPrefixes );
        prefixes.add( szName + "." );

        return ( (GramFactory)this.getClassFactory() ).popping(
                new FixScopeName(szName, prefixes, (List<String >)this.mPreloadSuffixes), this.getHeistgram()
        );
    }

    @Override
    protected List<Servgram > popping( Name name ) {
        return ( (GramFactory)this.getClassFactory() ).popping( name, this.getHeistgram() );
    }
}
