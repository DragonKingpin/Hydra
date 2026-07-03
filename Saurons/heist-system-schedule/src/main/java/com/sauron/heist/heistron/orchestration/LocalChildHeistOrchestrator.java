package com.sauron.heist.heistron.orchestration;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.name.Name;
import com.pinecone.hydra.servgram.*;
import com.sauron.heist.heistron.CascadeHeist;
import com.sauron.heist.heistron.Heistgram;
import com.sauron.heist.heistron.Heistium;
import com.sauron.heist.heistron.Heistum;
import com.sauron.heist.heistron.orchestration.provider.HeistletProviderRegistry;
import com.sauron.heist.heistron.orchestration.provider.HeistletResolveContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalChildHeistOrchestrator extends ArchServgramOrchestrator implements ChildHeistOrchestrator {
    private final AtomicInteger mAutoIncrementTaskId  = new AtomicInteger( 0 ) ;
    protected Heistium mHeistium                                                ;
    protected JSONConfig        mChildren                                                ;
    protected HeistletProviderRegistry mProviderRegistry = new HeistletProviderRegistry();

    public LocalChildHeistOrchestrator( Processum parent, PatriarchalConfig sectionConfig, @Nullable GramFactory factory, GramTransaction transaction ) {
        super( parent, sectionConfig, factory, transaction );
    }

    public LocalChildHeistOrchestrator( Processum parent, PatriarchalConfig sectionConfig ) {
        this( parent, sectionConfig, null, null );

        //this.prepareFactory( new LocalHeistletFactory( this ) );
        this.setTransaction( new LocalGramTransaction( this, parent ) );

        if( parent instanceof Heistium ) {
            this.mHeistium = (Heistium)parent;
            this.mChildren = this.getHeist().getConfig().getChild( Heistum.ConfigChildrenKey );
            this.mProviderRegistry = this.getHeist().getGramHeistletOrchestrator().providerRegistry();
        }
    }

    @Override
    public int nextAutoIncrementTaskId() {
        return this.mAutoIncrementTaskId.getAndIncrement();
    }

    @Override
    public Heistium getHeistium() {
        return this.mHeistium;
    }

    @Override
    public CascadeHeist getHeist() {
        return (CascadeHeist) this.getHeistium().getParentHeist();
    }

    @Override
    public Heistgram getHeistgram() {
        return this.getHeist().getHeistgram();
    }

    @Override
    public HeistletProviderRegistry providerRegistry() {
        return this.mProviderRegistry;
    }

    protected void applyChildModifier( CascadeHeist heistum ) {
        ChildHeistInstanceModifier modifier = this.getHeist().getChildHeistInstanceModifier();
        if ( modifier != null ) {
            heistum.applyChildHeistInstanceModifier( modifier );
            modifier.modify( heistum );
        }
    }

    @Override
    protected List<Servgram> popping( String szName ) {
        List<Servgram> list = new ArrayList<>();
        if( this.mChildren.hasOwnProperty( szName ) ) {
            List<Servgram> provided = this.providerRegistry().popping(
                    new HeistletResolveContext( this.getHeistgram(), this.getHeist(), szName, true )
            );
            if ( !provided.isEmpty() ) {
                for ( Servgram servgram : provided ) {
                    if ( servgram instanceof CascadeHeist ) {
                        this.applyChildModifier( (CascadeHeist) servgram );
                        this.infoLifecycle(  "Child contrived -> " + ( (CascadeHeist) servgram ).getInstanceFullName() ) ;
                    }
                }
                return provided;
            }

            try{
                CascadeHeist heistum = this.getHeist().getClass().getConstructor( Heistgram.class, CascadeHeist.class, String.class ).newInstance(
                        this.getHeistgram(), this.getHeist(), szName
                );

                this.applyChildModifier( heistum );

                this.infoLifecycle(  "Child contrived -> " + heistum.getInstanceFullName() ) ;
                list.add( heistum );
            }
            catch ( Exception e ) {
                this.tracer().warn( String.format( "[%s] Construct `%s` has been compromised.", this.className(), szName ), e );
            }
        }
        return list;
    }

    @Override
    protected List<Servgram > popping( Name name ) {
        return this.popping( name.getName() );
    }
}
