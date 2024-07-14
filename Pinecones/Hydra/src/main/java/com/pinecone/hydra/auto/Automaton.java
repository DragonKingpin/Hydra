package com.pinecone.hydra.auto;

import com.pinecone.framework.system.executum.Processum;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Automaton extends ArchAutomatron implements LifecycleAutomaton {
    private static final AtomicInteger nextSerialNumber = new AtomicInteger( 0 );

    private static int serialNumber() {
        return Automaton.nextSerialNumber.getAndIncrement();
    }

    private static String name( String name ) {
        if( name == null ) {
            return Automaton.class.getSimpleName() + "-" + Automaton.serialNumber();
        }
        return name;
    }

    private Thread                                mMasterThread       ;
    private AtomicLong                            mMaxLifetimeMillis  ;
    private AtomicLong                            mHeartbeatTimeoutMillis;

    protected volatile boolean                    mRunning            ;
    protected long                                mnCurrentPipelineWaitingMillis;
    protected final BlockingDeque<Instructation > mInstructationQueue ;

    protected Automaton( String szName, Processum parent, BlockingDeque<Instructation > deque, boolean bIsDaemon, long nCurrentPipelineWaitingMillis ) {
        super( Automaton.name( szName ), parent );

        this.mMaxLifetimeMillis             = new AtomicLong( 0 );
        this.mHeartbeatTimeoutMillis        = new AtomicLong( 0 );
        this.mnCurrentPipelineWaitingMillis = nCurrentPipelineWaitingMillis;
        this.mInstructationQueue            = deque;
        this.mMasterThread                  = new Thread( this::mainLoop );

        this.mMasterThread.setDaemon( bIsDaemon );
        this.mMasterThread.setName( this.mszName + this.mMasterThread.getName() );
        this.setThreadAffinity( this.mMasterThread );
    }

    public Automaton( String szName, Processum parent, boolean bIsDaemon, long nCurrentPipelineWaitingMillis ) {
        this( szName, parent, new LinkedBlockingDeque<>(), bIsDaemon, nCurrentPipelineWaitingMillis  );
    }

    public Automaton( Processum parent, boolean bIsDaemon, long nCurrentPipelineWaitingMillis ) {
        this( null, parent, bIsDaemon, nCurrentPipelineWaitingMillis );
    }

    public Automaton( String szName, Processum parent, long nCurrentPipelineWaitingMillis  ) {
        this( szName, parent, false, nCurrentPipelineWaitingMillis );
    }

    public Automaton( Processum parent, long nCurrentPipelineWaitingMillis ) {
        this( null, parent, nCurrentPipelineWaitingMillis );
    }


    public Automaton( String szName, Processum parent, boolean bIsDaemon ) {
        this( szName, parent, bIsDaemon, 50  );
    }

    public Automaton( Processum parent, boolean bIsDaemon ) {
        this( null, parent, bIsDaemon );
    }

    public Automaton( String szName, Processum parent  ) {
        this( szName, parent, false );
    }

    public Automaton( Processum parent ) {
        this( null, parent );
    }

    @Override
    public void start() {
        this.mRunning      = true;
        this.mMasterThread.start();
    }

    @Override
    public void join() throws InterruptedException {
        this.mMasterThread.join();
    }

    @Override
    public void join( long millis ) throws InterruptedException {
        this.mMasterThread.join( millis );
    }

    @Override
    public void command( Instructation instructation ) {
        this.mInstructationQueue.addLast( instructation );
    }

    @Override
    public void prompt( Instructation instructation ) {
        this.mInstructationQueue.addFirst( instructation );
    }

    @Override
    public void withdraw( Instructation instructation ) {
        this.mInstructationQueue.remove( instructation );
    }

    @Override
    public boolean isEnded() {
        return !this.mRunning;
    }

    @Override
    protected void intoEnded() {
        this.mRunning = false;
    }

    @Override
    public long getMaxLifetimeMillis() {
        return this.mMaxLifetimeMillis.get();
    }

    @Override
    public LifecycleAutomaton setMaxLifetimeMillis( long maxLifetimeMillis ) {
        this.mMaxLifetimeMillis.getAndSet( maxLifetimeMillis );
        return this;
    }

    @Override
    public long getHeartbeatTimeoutMillis() {
        return this.mHeartbeatTimeoutMillis.get();
    }

    @Override
    public LifecycleAutomaton setHeartbeatTimeoutMillis( long heartbeatTimeoutMillis ) {
        this.mHeartbeatTimeoutMillis.getAndSet( heartbeatTimeoutMillis );
        return this;
    }

    protected void mainLoop() {
        long startTime       = System.currentTimeMillis();
        long lastCommandTime = System.currentTimeMillis();

        while ( this.mRunning ) {
            try{
                if( Thread.currentThread().isInterrupted() ) {
                    throw new AbortException();
                }

                long currentTime = System.currentTimeMillis();
                if ( this.getMaxLifetimeMillis() > 0 && ( currentTime - startTime ) > this.getMaxLifetimeMillis() ) {
                    this.intoEnded();  // Suicide
                    break;
                }

                if ( this.getHeartbeatTimeoutMillis() > 0 && ( currentTime - lastCommandTime ) > this.getHeartbeatTimeoutMillis() ) {
                    this.intoEnded(); // Suicide
                    break;
                }

                Instructation instructation = this.mInstructationQueue.poll( this.mnCurrentPipelineWaitingMillis, TimeUnit.MILLISECONDS );
                if ( instructation != null ) {
                    try {
                        instructation.execute();
                    }
                    catch ( ContinueException c ) {
                        // Do nothing
                    }

                    lastCommandTime = System.currentTimeMillis(); // Reset heartbeat timeout
                }
            }
            catch ( Exception e ) {
                try{
                    this.handleException( e );
                }
                catch ( ContinueException c ) {
                    // Do nothing
                }
                catch ( Exception ke ) {
                    break;
                }
            }
        }
    }
}
