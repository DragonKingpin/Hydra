package com.pinecone.hydra.auto;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.Debug;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PeriodicAutomaton extends ArchAutomatron implements PeriodicAutomatron {
    private static final AtomicInteger nextSerialNumber = new AtomicInteger( 0 );

    private static int serialNumber() {
        return PeriodicAutomaton.nextSerialNumber.getAndIncrement();
    }

    private static String name( String name ) {
        if( name == null ) {
            return PeriodicAutomaton.class.getSimpleName() + "-" + PeriodicAutomaton.serialNumber();
        }
        return name;
    }

    private Marshalling                   mMarshalling;
    private final AtomicLong              mPeriodMillis;
    private Thread                        mMasterThread;

    protected Deque<InstructLine >        mInstructationBuffer;
    protected ReentrantReadWriteLock      mBufferLock;
    protected final Object                mNextPeriodLock = new Object();
    protected volatile boolean            mRunning;


    protected PeriodicAutomaton( String szName, Processum parent, Marshalling marshalling, Deque<InstructLine > buffer, long nPeriodMillis, boolean bIsDaemon, ExceptionHandler handler ) {
        super( PeriodicAutomaton.name( szName ), parent, handler );

        this.mBufferLock          = new ReentrantReadWriteLock();
        this.mPeriodMillis        = new AtomicLong( nPeriodMillis );
        this.mMarshalling         = marshalling;
        this.mInstructationBuffer = buffer;
        this.mMasterThread        = new Thread( this::mainLoop );

        this.mMasterThread.setDaemon( bIsDaemon );
        this.mMasterThread.setName( this.mszName + this.mMasterThread.getName() );
        this.setThreadAffinity( this.mMasterThread );
    }

    public PeriodicAutomaton( String szName, Processum parent, Marshalling marshalling, long nPeriodMillis, boolean bIsDaemon, ExceptionHandler handler ) {
        this( szName, parent, marshalling, new LinkedList<>(),nPeriodMillis, bIsDaemon, handler  );
    }

    public PeriodicAutomaton( String szName, Processum parent, Marshalling marshalling, long nPeriodMillis, boolean bIsDaemon ) {
        this( szName, parent, marshalling,nPeriodMillis, bIsDaemon, null  );
    }

    public PeriodicAutomaton( String szName, Processum parent, ExceptionHandler handler, long nPeriodMillis, boolean bIsDaemon ) {
        this( szName, parent, new GenericMarshalling(), nPeriodMillis, bIsDaemon, handler  );
    }

    public PeriodicAutomaton( String szName, Processum parent, long nPeriodMillis, boolean bIsDaemon ) {
        this( szName, parent, (ExceptionHandler) null, nPeriodMillis, bIsDaemon );
    }

    public PeriodicAutomaton( Processum parent, long nPeriodMillis, boolean bIsDaemon ) {
        this( null, parent, (ExceptionHandler) null, nPeriodMillis, bIsDaemon );
    }

    public PeriodicAutomaton( Processum parent, long nPeriodMillis ) {
        this( null, parent, (ExceptionHandler) null, nPeriodMillis, false );
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
    public boolean isEnded() {
        return !this.mRunning;
    }

    protected void invokeIfKernelInstructation( Instructation instructation ) throws Exception {
        if( instructation == KernelInstructation.DIE ) {
            instructation.execute();
        }
    }

    @Override
    protected void intoEnded() {
        this.mRunning = false;
    }

    protected void fetchCacheIntoMarshalling( boolean bLoopMode ) {
        // <Ref::Marshalling>, locked writing operations.
        this.mBufferLock.writeLock().lock();
        try {
            if ( this.mInstructationBuffer.isEmpty() ) {
                return;
            }

            for( InstructLine line : this.mInstructationBuffer ) {
                if( line.setRemove ) {
                    if( bLoopMode ) {
                        this.invokeIfKernelInstructation( line.instructation );
                    }

                    if( line.instructation instanceof InstantInstructation ) {
                        this.mMarshalling.prompt( line.instructation );
                    }
                    else {
                        if( line.piror ) {
                            this.mMarshalling.addFirst( line.instructation );
                        }
                        else {
                            this.mMarshalling.addLast( line.instructation );
                        }
                    }
                }
                else {
                    this.mMarshalling.erase( line.instructation );
                }
            }
            this.mInstructationBuffer.clear();
        }
        catch ( Exception e ) {
            this.handleException( e );
        }
        finally {
            this.mBufferLock.writeLock().unlock();
        }
    }

    protected void mainLoop() {
        try{
            while ( this.mRunning ) {
                try {
                    long startTime = System.currentTimeMillis();

                    if( Thread.currentThread().isInterrupted() ) {
                        throw new AbortException();
                    }

                    this.fetchCacheIntoMarshalling( true );
                    if( !this.mRunning ) { // Check if given `death` instruction.
                        break;
                    }

                    // <Ref::Marshalling>, locked writing operations.
                    // this(Consumer, Who are trying to consume all commands) and others (Producer, Who are trying to add new command)
                    this.mBufferLock.readLock().lock();
                    try{
                        try{
                            this.mMarshalling.execute();
                        }
                        catch ( ContinueException c ) {
                            // Do nothing
                        }
                    }
                    finally {
                        this.mBufferLock.readLock().unlock();
                    }


                    //Debug.echo( "\n" );

                    long endTime = System.currentTimeMillis();
                    long elapsed = endTime - startTime;
                    long sleepTime = this.mPeriodMillis.get() - elapsed;

                    if ( sleepTime > 0 ) {
                        synchronized ( this.mNextPeriodLock ) {
                            this.mNextPeriodLock.wait( sleepTime );
                        }
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
        finally {
            synchronized ( this.mNextPeriodLock ) {
                this.mNextPeriodLock.notify();
            }
        }
    }

    protected boolean tryLockBuffer() {
        boolean bHeldByCurrentThread = this.mBufferLock.writeLock().isHeldByCurrentThread();
        if( !bHeldByCurrentThread ) {
            bHeldByCurrentThread = Thread.currentThread() == this.mMasterThread;
        }
        boolean bOptLocked = true;
        if( bHeldByCurrentThread ) {
            bOptLocked = this.mBufferLock.writeLock().tryLock();
        }
        else {
            this.mBufferLock.writeLock().lock();
        }
        return bOptLocked;
    }

    protected void add ( Instructation instructation, boolean bPrior ) {
        boolean bOptLocked = this.tryLockBuffer();

        try{
            if ( !this.mRunning ) {
                // <Ref::Marshalling>, locked writing operations.
                this.mMarshalling.add( instructation );
            }
            else {
                if( Thread.currentThread() != this.mMasterThread ) {  // Nested operation
                    this.fetchCacheIntoMarshalling( false );
                }

                InstructLine line = new InstructLine( instructation, bPrior, true );
                if( bPrior ) {
                    this.mInstructationBuffer.addFirst( line );
                }
                else {
                    this.mInstructationBuffer.addLast( line );
                }
            }
        }
        finally {
            if( bOptLocked && this.mBufferLock.writeLock().getHoldCount() > 0 ) {
                this.mBufferLock.writeLock().unlock();
            }
        }
    }

    @Override
    public void command( Instructation instructation ) {
        this.add( instructation, false );
    }

    @Override
    public void prompt( Instructation instructation ) {
        this.add( instructation, true );
    }

    @Override
    public void withdraw( Instructation instructation ) {
        boolean bOptLocked = this.tryLockBuffer();

        try {
            InstructLine target = null;
            for( InstructLine line : this.mInstructationBuffer ) {
                if( line.instructation.equals( instructation ) ) {
                    target = line;
                    break;
                }
            }

            if( target != null ) {
                this.mInstructationBuffer.remove( target );
            }

            if ( !this.mRunning ) {
                // <Ref::Marshalling>, locked writing operations.
                this.mMarshalling.erase( instructation );
            }
            else {
                if( Thread.currentThread() != this.mMasterThread ) {  // Nested operation
                    this.fetchCacheIntoMarshalling( false );
                }

                InstructLine line = new InstructLine( instructation, false, false );
                this.mInstructationBuffer.addLast( line );
            }
        }
        finally {
            if( bOptLocked && this.mBufferLock.writeLock().getHoldCount() > 0 ) {
                this.mBufferLock.writeLock().unlock();
            }
        }
    }

    @Override
    public long getPeriodMillis() {
        return this.mPeriodMillis.get();
    }

    @Override
    public void setPeriodMillis( long periodMillis ) {
        this.mPeriodMillis.getAndSet( periodMillis );
    }

    @Override
    public Collection<Instructation > getBuffer() {
        ArrayList<Instructation > list = new ArrayList<>();
        for( InstructLine line : this.mInstructationBuffer ) {
            list.add( line.instructation );
        }
        return list;
    }

    @Override
    public int bufferSize() {
        return this.mInstructationBuffer.size();
    }

    @Override
    public Marshalling getMarshalling() {
        return this.mMarshalling;
    }

    @Override
    public Thread getMasterThread() {
        return this.mMasterThread;
    }

    protected class InstructLine {
        protected Instructation instructation;
        protected boolean       piror;
        protected boolean       setRemove; // 0 => remove, 1 => set

        protected InstructLine ( Instructation instructation, boolean piror, boolean setRemove ) {
            this.instructation = instructation;
            this.piror         = piror;
            this.setRemove     = setRemove;
        }
    }
}
