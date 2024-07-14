package com.pinecone.hydra.orchestration;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.orchestration.regulation.NeglectRegulation;
import com.pinecone.hydra.orchestration.regulation.RuntimeNeglector;

import java.util.ArrayList;
import java.util.List;

public abstract class ArchTransaction extends ArchStratum implements Transaction {
    protected Exertium               mExertium; // To implement GraphNode and Exertion.
    protected NeglectRegulation      mSequentialRuntimeNeglector;
    protected ExertionEventCallback  mExertionStartCB;
    protected ExertionEventCallback  mExertionEndCB;

    protected ArchTransaction() {
        super();
        this.mExertium                     = new Exertium();
        this.mSequentialRuntimeNeglector   = new RuntimeNeglector( this );
    }

    @Override
    public NeglectRegulation getSeqExceptionNeglector() {
        return this.mSequentialRuntimeNeglector;
    }

    @Override
    public void setSeqExceptionNeglector( NeglectRegulation neglector ) {
        this.mSequentialRuntimeNeglector = neglector;
    }

    protected Exertium getExertium() {
        return this.mExertium;
    }

    @Override
    public void registerExertionStartCallback( ExertionEventCallback callback ) {
        this.mExertionStartCB = callback;
    }

    @Override
    public void registerExertionEndCallback( ExertionEventCallback callback ) {
        this.mExertionEndCB = callback;
    }

    @Override
    public void setDefaultRollback( boolean b ) {
        this.getExertium().setDefaultRollback( b );
    }

    @Override
    public boolean isDefaultRollback() {
        return this.getExertium().isDefaultRollback();
    }

    @Override
    public String getName(){
        return this.getExertium().getName();
    }

    @Override
    public void setName( String name ) {
        this.getExertium().setName( name );
    }

    @Override
    public IntegrityLevel getIntegrityLevel(){
        return this.getExertium().getIntegrityLevel();
    }

    @Override
    public void setIntegrityLevel( IntegrityLevel level ){
        this.getExertium().setIntegrityLevel( level );
    }

    @Override
    public long getStartNano() {
        return this.getExertium().getStartNano();
    }

    @Override
    public int getStratumId() {
        return this.getExertium().getStratumId();
    }

    protected void beforeAdd( Exertion exertion ) {
        if( exertion instanceof ArchGraphNode ) {
            ((ArchGraphNode) exertion).setParent( this );
        }
    }

    @Override
    public void add( Exertion exertion ) {
        this.beforeAdd( exertion );
        this.getChildren().add( exertion );
    }

    @Override
    public void addFirst( Exertion exertion ) {
        this.beforeAdd( exertion );
        this.getChildren().add( 0, exertion );
    }

    @Override
    public ExertionStatus getStatus() {
        return this.getExertium().getStatus();
    }

    @Override
    public Exception getLastError() {
        return this.mExertium.getLastError();
    }
}
