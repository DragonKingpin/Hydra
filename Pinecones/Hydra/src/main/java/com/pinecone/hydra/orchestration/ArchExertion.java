package com.pinecone.hydra.orchestration;

import com.pinecone.framework.util.Debug;

public abstract class ArchExertion extends ArchGraphNode implements Exertion {
    protected String                 mszName;
    protected boolean                mbDefaultRollback = false;
    protected IntegrityLevel         mIntegrityLevel   = IntegrityLevel.Strict;
    protected ExertionStatus         mStatus           = ExertionStatus.NEW;
    protected Exception              mLastError        = null;
    protected long                   mnStartNano             ;

    protected ArchExertion() {
        this.mnStartNano = System.nanoTime();
    }

    @Override
    public void reset() {
        this.mStatus = ExertionStatus.NEW;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public void setName( String name ) {
        this.mszName = name;
    }

    @Override
    public IntegrityLevel getIntegrityLevel(){
        return this.mIntegrityLevel;
    }

    @Override
    public void setIntegrityLevel( IntegrityLevel level ){
        this.mIntegrityLevel = level;
    }

    @Override
    public long getStartNano() {
        return this.mnStartNano;
    }

    @Override
    public void setDefaultRollback( boolean b ){
        this.mbDefaultRollback = b;
    }

    @Override
    public boolean isDefaultRollback() {
        return this.mbDefaultRollback;
    }

    @Override
    public ExertionStatus getStatus() {
        return this.mStatus;
    }

    protected void intoStart() {
        this.mStatus = ExertionStatus.RUNNING;
    }

    protected void intoFinished() {
        this.mStatus = ExertionStatus.FINISHED;
    }

    protected void intoTerminated() {
        this.mStatus = ExertionStatus.TERMINATED;
    }

    protected void intoRollback() {
        this.mStatus = ExertionStatus.ROLLING;
    }

    protected void intoError( Exception e ) {
        this.mStatus    = ExertionStatus.ERROR;
        this.mLastError = e;
    }

    @Override
    public Exception getLastError() {
        return this.mLastError;
    }

    protected abstract void doStart();

    protected abstract void doTerminate() ;

    protected abstract void doRollback();

    protected boolean handleErrorCondition( Exception e ) {
        if( this.mIntegrityLevel != IntegrityLevel.Strict ) {
            if( this.mIntegrityLevel == IntegrityLevel.Warning ) {
                Debug.warn( "TODO", e, e.getMessage() ); // TODO
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        this.intoStart();
        try{
            this.doStart();
            this.intoFinished();
        }
        catch ( Exception e ) {
            if( this.handleErrorCondition( e ) ) {
                this.intoFinished();
            }
            else {
                this.intoError( e );
            }
        }
    }

    @Override
    public void terminate() {
        try{
            this.doTerminate();
            this.intoTerminated();
        }
        catch ( Exception e ) {
            if( this.handleErrorCondition( e ) ) {
                this.intoTerminated();
            }
            else {
                this.intoError( e );
            }
        }
    }

    @Override
    public void rollback() {
        this.intoRollback();
        try{
            this.doRollback();
            this.intoFinished();
        }
        catch ( Exception e ) {
            if( this.handleErrorCondition( e ) ) {
                this.intoFinished();
            }
            else {
                this.intoError( e );
            }
        }
    }
}
