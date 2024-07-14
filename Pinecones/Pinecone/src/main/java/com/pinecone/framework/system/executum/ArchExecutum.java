package com.pinecone.framework.system.executum;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.GenericMasterTaskManager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ArchExecutum implements Executum {
    private int               mnId                      ;
    protected String          mszName                   ;
    protected RuntimeSystem   mParentSystem             ;
    protected Processum       mParentProcessum          ;

    protected Thread          mAffiliateThread          ;

    protected int             mExceptionRestartCount    = 0;
    protected int             mExceptionRestartTime     ; // < 0 Force always keep alive, like the cancer.

    // Mutex & Lock
    protected ReentrantReadWriteLock mResourceLock     = new ReentrantReadWriteLock();

    protected ArchExecutum ( String szName, Processum parent, Thread affiliateThread ) {
        this.mAffiliateThread = affiliateThread;
        this.mszName          = szName;
        this.mParentProcessum = parent;
        if( this.mParentProcessum == null ) {
            this.mParentSystem = null;
        }
        else if( this.mParentProcessum instanceof RuntimeSystem ) {
            this.mParentSystem = (RuntimeSystem) this.mParentProcessum;
        }
        else {
            this.mParentSystem = this.mParentProcessum.getSystem();
        }

        this.makeNameAndId();
    }

    protected ArchExecutum ( String szName, Processum parent ) {
        this( szName, parent, null );
    }

    protected ArchExecutum ( Processum parent, Thread affiliateThread ) {
        this( affiliateThread.getName(), parent, affiliateThread );
    }

    protected void makeNameAndId() {
        this.mnId          = Executum.nextAutoIncrementId();
        if( this.mszName == null ) {
            this.mszName = this.className();
            long id = this.getId();
            if( this.mParentProcessum != null ) {
                this.mszName = this.mszName + "-Executum-" + id;
            }
        }
    }


    @Override
    public int getExceptionRestartTime() {
        return this.mExceptionRestartTime;
    }

    @Override
    public ArchExecutum applyExceptionRestartTime( int time ){
        this.mResourceLock.writeLock().lock();
        this.mExceptionRestartTime = time;
        this.mResourceLock.writeLock().unlock();
        return this;
    }

    @Override
    public String getName(){
        return this.mszName;
    }

    @Override
    public void setName( String szName ) {
        this.mszName = szName;
    }

    @Override
    public int getId() {
        return this.mnId;
    }

    @Override
    public ArchExecutum setThreadAffinity( Thread affinity ) {
        this.mAffiliateThread = affinity;
        return this;
    }

    @Override
    public Thread getAffiliateThread() {
        return this.mAffiliateThread;
    }

    @Override
    public Thread.State getState() {
        return this.getAffiliateThread().getState();
    }

    @Override
    public RuntimeSystem  getSystem() {
        return this.mParentSystem;
    }

    @Override
    public Processum parentExecutum() {
        return this.mParentProcessum;
    }

    @Override
    public boolean isTerminated(){
        return this.getState() == Thread.State.TERMINATED;
    }
}
