package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.ApoptosisRejectSignalException;
import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.orchestration.*;
import com.pinecone.hydra.orchestration.regulation.NeglectRegulation;

import java.util.List;

public class LocalGramTransaction extends ArchProcessum implements GramTransaction, Processum {
    private   List                    mActionList   ;
    private   ActionType              mActionType   ;
    protected ServgramOrchestrator    mOrchestrator ;
    protected Transaction             mTransaction  ;

    public LocalGramTransaction( String name, ServgramOrchestrator orchestrator, Processum parent ) {
        super( name, parent );
        this.mOrchestrator = orchestrator;
        this.mActionType   = ActionType.queryActionType( this.mOrchestrator.getOrchestrationConfig().get( ActionType.ConfigActionTypeKey ).toString() );
        this.mActionList   = (List)((JSONObject) orchestrator.getOrchestrationConfig()).get( GramTransaction.ConfigTransactionsListKey );

        this.prepareTransactionByType();
        this.setName( name );

        orchestrator.tracer().info( String.format( "[Lifecycle] [%s, %s] <Contrived>", name, this.mActionType ) );
    }

    public LocalGramTransaction( ServgramOrchestrator orchestrator, Processum parent ) {
        this( orchestrator.getOrchestrationConfig().getOrDefault( GramTransaction.ConfigTransactionNameKey, "Anonymous" ).toString(), orchestrator, parent );
    }

    // Children Transaction
    public LocalGramTransaction( String name, ActionType actionType, List actionList, ServgramOrchestrator orchestrator, Processum parent ) {
        super( name, parent );
        this.mOrchestrator = orchestrator;
        this.mActionType   = actionType;
        this.mActionList   = actionList;

        this.prepareTransactionByType();
        this.setName( name );
    }


    protected void prepareTransactionByType() {
        switch ( this.mActionType ) {
            case Loop: {
                this.mTransaction = new LoopAction();
                break;
            }
            case Parallel:{
                this.mTransaction = new ParallelAction();
                break;
            }
            case Sequential:{
                this.mTransaction = new SequentialAction();
                break;
            }
            default: {
                throw new IllegalArgumentException( "MasterTransaction can only be [Loop, Parallel, Sequential]." );
            }
        }
    }

    @Override
    public void apoptosis() throws ApoptosisRejectSignalException {
        this.terminate();
    }

    @Override
    public void kill() {
        this.terminate();
        if( !this.isEnded() ) {
            super.kill();
        }
    }

    @Override
    public GramTransaction loadActionsFromConfig() {
        return this;
    }

    @Override
    public List getTransactionList() {
        return this.mActionList;
    }

    @Override
    public void add( Exertion exertion ) {
        this.mTransaction.add(exertion);
    }

    @Override
    public void addFirst( Exertion exertion ) {
        this.mTransaction.addFirst(exertion);
    }

    @Override
    public void reset() {
        this.mTransaction.reset();
    }

    @Override
    public void start() {
        this.mTransaction.start();
    }

    @Override
    public void terminate() {
        this.mTransaction.terminate();
    }

    @Override
    public void rollback() {
        this.mTransaction.rollback();
    }

    @Override
    public NeglectRegulation getSeqExceptionNeglector(){
        return this.mTransaction.getSeqExceptionNeglector();
    }

    @Override
    public void setSeqExceptionNeglector( NeglectRegulation neglector ) {
        this.mTransaction.setSeqExceptionNeglector( neglector );
    }

    @Override
    public ExertionStatus getStatus() {
        return this.mTransaction.getStatus();
    }

    @Override
    public String getName(){
        return this.mTransaction.getName();
    }

    @Override
    public void setName( String name ){
        this.mTransaction.setName( name );
    }

    @Override
    public IntegrityLevel getIntegrityLevel(){
        return this.mTransaction.getIntegrityLevel();
    }

    @Override
    public void setIntegrityLevel( IntegrityLevel level ){
        this.mTransaction.setIntegrityLevel( level );
    }

    @Override
    public long getStartNano() {
        return this.mTransaction.getStartNano();
    }

    @Override
    public void setDefaultRollback( boolean b ){
        this.mTransaction.setDefaultRollback( b );
    }

    @Override
    public boolean isDefaultRollback(){
        return this.mTransaction.isDefaultRollback();
    }

    @Override
    public int getStratumId(){
        return this.mTransaction.getStratumId();
    }

    @Override
    public ArchGraphNode parent(){
        return (ArchGraphNode)this.mTransaction.parent();
    }

    @Override
    public List<GraphNode > getChildren() {
        return ( (GraphStratum)this.mTransaction ).getChildren();
    }

    @Override
    public Exception getLastError() {
        return this.mTransaction.getLastError();
    }

    @Override
    public void registerExertionStartCallback( ExertionEventCallback callback ) {
        this.mTransaction.registerExertionStartCallback( callback );
    }

    @Override
    public void registerExertionEndCallback( ExertionEventCallback callback ) {
        this.mTransaction.registerExertionEndCallback( callback );
    }
}
