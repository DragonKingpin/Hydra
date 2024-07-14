package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.*;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.genius.util.SystemUtils;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.name.Name;
import com.pinecone.hydra.orchestration.Exertion;
import com.pinecone.hydra.orchestration.IntegrityLevel;
import com.pinecone.ulf.util.lang.MultiScopeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ArchServgramOrchestrator extends GenericMasterTaskManager implements ServgramOrchestrator {
    private Logger              logger                ;
    private GramFactory         mGramFactory          ;
    private PatriarchalConfig   mSectionConfig        ;
    private PatriarchalConfig   mOrchestrationConfig  ;
    private List                mServgramScopesConf   ;
    private GramTransaction     mTransaction          ;
    private Exertion            mPrimaryAction        ;
    private ReentrantLock       mNotifyLock           ;

    public ArchServgramOrchestrator( Processum parent, PatriarchalConfig sectionConfig, @Nullable GramFactory factory, GramTransaction transaction ) {
        super( parent );

        this.mSectionConfig            = sectionConfig;
        this.logger                    = LoggerFactory.getLogger( String.format( "Tracer<%s>", this.className() ) );
        this.mOrchestrationConfig      = this.mSectionConfig.getChild( Orchestrator.ConfigOrchestrationKey );
        this.mServgramScopesConf       = (List)((JSONObject) this.mOrchestrationConfig).get( Orchestrator.ConfigServgramScopesKey );
        this.mNotifyLock               = new ReentrantLock();

        this.prepareFactory( factory );
        this.mTransaction              = transaction;
    }

    protected void prepareFactory( @Nullable GramFactory factory ) {
        if( factory != null ) {
            this.mGramFactory              = factory;
            for( Object o : this.mServgramScopesConf ) {
                this.mGramFactory.getClassScope().addScope( o.toString() );
            }
            this.mGramFactory.getTraitClassLoader().updateScope();
        }
    }

    @Override
    public Pinecore getSystem() {
        return (Pinecore) super.getSystem();
    }

    @Override
    public PatriarchalConfig getSectionConfig() {
        return this.mSectionConfig;
    }

    @Override
    public PatriarchalConfig getScheme(){
        return this.getSectionConfig();
    }

    @Override
    public PatriarchalConfig getOrchestrationConfig() {
        return this.mOrchestrationConfig;
    }

    @Override
    public GramTransaction getTransaction() {
        return this.mTransaction;
    }

    @Override
    public void setTransaction( GramTransaction transaction ) {
        this.mTransaction = transaction;
    }

    @Override
    public MultiScopeFactory getClassFactory() {
        return this.mGramFactory;
    }

    protected Exertion wrapServgramAction( Servgram servgram, ActionType type ) {
        switch ( type ) {
            case Parallel: {
                return new LocalParallelGramExertium( this, servgram );
            }
            case Sequential:{
                return new LocalSequentialGramExertium( this,servgram );
            }
            default:{
                throw new IllegalArgumentException( "ServgramAction can only be [Parallel, Sequential]." );
            }
        }
    }

    protected void orchestrateTransactionGroup( GramTransaction transaction, GramTransaction.TransactionArgs args, Map protoConf, int stratum ) throws OrchestrateInterruptException {
        List childrenList = (List) protoConf.get( GramTransaction.ConfigTransactionsListKey );
        GramTransaction child = new LocalGramTransaction( args.name, args.type.reinterpretActions(), childrenList, this, this.getParentProcessum() );
        this.orchestrateTransaction( child, stratum + 1 );
        transaction.add( child );
    }

    protected abstract List<Servgram > popping( String szName );

    protected abstract List<Servgram > popping( Name name );

    protected void orchestrateServgramium( GramTransaction transaction, GramTransaction.TransactionArgs args, Map protoConf, boolean bPrimary, int stratum ) throws OrchestrateInterruptException {
        List<Servgram > grams = this.popping( args.name );
        if( grams.isEmpty() ) {
            this.tracer().warn( "[Lifecycle] <None of servgrams been found.>" );
        }

        if( bPrimary ) {
            if( stratum > 0 && this.mPrimaryAction != null ) {
                throw new OrchestrateInterruptException( "Primary Servgram can only have one instance in the whole transaction graph." );
            }
            if( grams.size() > 1 ) {
                throw new OrchestrateInterruptException( "Primary Servgram can only have one instance." );
            }
            else {
                if( !grams.isEmpty() ){
                    this.mPrimaryAction = this.wrapServgramAction( grams.get(0), args.type );
                    this.mPrimaryAction.setIntegrityLevel( args.level );
                    this.mTransaction.addFirst( this.mPrimaryAction );
                }
            }
        }
        else {
            for( Servgram gram : grams ) {
                Exertion act = this.wrapServgramAction( gram, args.type );
                act.setIntegrityLevel( args.level );
                transaction.add( act );
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    protected void orchestrateTransaction( GramTransaction transaction, int stratum ) throws OrchestrateInterruptException {
        try{
            for( Object o : transaction.getTransactionList() ) {
                Map jo = (Map) o;
                GramTransaction.TransactionArgs args = new GramTransaction.TransactionArgs(
                        (String) jo.getOrDefault( GramTransaction.ConfigTransactionNameKey, "" ),
                        ActionType.queryActionType( (String) jo.getOrDefault( ActionType.ConfigActionTypeKey, ActionType.Sequential.getName() ) ),
                        IntegrityLevel.queryIntegrityLevel( jo.getOrDefault( IntegrityLevel.ConfIntegrityLevelKey, IntegrityLevel.Warning ).toString() )
                );

                boolean    bPrimary = (boolean) jo.getOrDefault( GramTransaction.ConfigPrimaryNameKey, false );
                if( args.type.isActionGroup() ) {
                    this.orchestrateTransactionGroup( transaction, args, jo, stratum );
                }
                else {
                    this.orchestrateServgramium( transaction, args, jo, bPrimary, stratum );
                }
            }
        }
        catch ( Exception e ) {
            throw new OrchestrateInterruptException( "Orchestrate has been interrupted, transaction compromised.", e );
        }
    }

    public void onlyOrchestrateTransaction() throws OrchestrateInterruptException {
        this.orchestrateTransaction( this.mTransaction, 0 );
    }

    @Override
    public void orchestrate() throws OrchestrateInterruptException {
        this.infoLifecycle( "Executing designed orchestration sequence" );

        this.onlyOrchestrateTransaction();
        this.startTransaction();
    }

    /**
     * startTransaction
     * Transaction should be joined or sequential with atomic trait.
     * Default transaction set the affinity to its parent thread, with sequential trait.
     * If you wish to set the affinity to a master thread, please overriding this method.
     */
    @Override
    public void startTransaction() throws OrchestrateInterruptException {
        if( this.mTransaction instanceof Executum ) {
            this.add( (Executum) this.mTransaction );
            ((Executum) this.mTransaction).setThreadAffinity( Thread.currentThread() );
        }

        this.mTransaction.start();

        if( this.mTransaction instanceof Executum ) {
            this.erase( (Executum) this.mTransaction );
        }
    }

    @Override
    public List    preloads( String szName ) {
        return this.popping( szName );
    }

    @Override
    public List    preloads( Name name ) {
        return this.popping( name );
    }

    @Override
    public Logger tracer() {
        return this.logger;
    }

    protected ArchServgramOrchestrator infoLifecycle( String szWhat, String szStateOrExtra ) {
        this.tracer().info( "[Lifecycle] [{}] <{}>", szWhat, szStateOrExtra );
        return this;
    }

    protected ArchServgramOrchestrator infoLifecycle( String szStateOrExtra ) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return this.infoLifecycle( stackTraceElements[ 2 ].getMethodName(), szStateOrExtra );
    }

    @Override
    public void notifyFinished  ( Executum that ) {
        this.mNotifyLock.lock();
        try{
            this.erase( that );
        }
        finally {
            this.mNotifyLock.unlock();
        }

        super.notifyFinished( that );
    }

    @Override
    public void notifyExecuting ( Executum that ) {
        this.mNotifyLock.lock();
        try{
            this.add( that );
        }
        finally {
            this.mNotifyLock.unlock();
        }

        super.notifyExecuting( that );
    }
}
