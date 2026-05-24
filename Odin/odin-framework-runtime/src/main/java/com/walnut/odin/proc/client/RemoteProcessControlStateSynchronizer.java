package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.proc.LocalUProcess;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.control.RemoteProcessControlFrame;
import com.walnut.odin.proc.control.RemoteProcessControlFrameIface;
import com.walnut.odin.proc.control.RemoteProcessControlFrameType;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RemoteProcessControlStateSynchronizer implements Pinenut {

    protected static final long                   RetryDelayMillis0 = 500;

    protected static final long                   RetryDelayMillis1 = 1000;

    protected static final long                   RetryDelayMillis2 = 2000;

    protected static final long                   AsyncSynchronizeQuietMillis = 1000;

    protected static final long                   ControlSyncRpcTimeoutMillis = 5000;

    protected static final String                 ControlFrameIfaceAddressPrefix = RemoteProcessControlFrameIface.class.getName() + ".";

    protected RavenRemoteProcessManagerClient    mClient;

    protected RemoteProcessControlFrameIface     mControlFrameIface;

    protected ReentrantLock                      mSyncLock = new ReentrantLock();

    protected Condition                          mSyncFinishedCondition = this.mSyncLock.newCondition();

    protected Condition                          mRetryCondition = this.mSyncLock.newCondition();

    protected boolean                            mbSynchronizing;

    protected boolean                            mbResyncRequested;

    protected String                             mszSessionGuid;

    public RemoteProcessControlStateSynchronizer( RavenRemoteProcessManagerClient client, RemoteProcessControlFrameIface controlFrameIface ) {
        this.mClient            = client;
        this.mControlFrameIface = controlFrameIface;
    }

    protected String nextGuidString() {
        return this.mClient.getGuidAllocator().nextGUID().toString();
    }

    public void requestSynchronize( String szReason ) {
        if ( !this.startSynchronizing() ) {
            this.mClient.getLogger().info(
                    "[RemoteProcessControlSync] (Reason: `{}`, ClientId: `{}`) <AlreadySynchronizing>",
                    szReason,
                    this.mClient.getClientId()
            );
            this.requestResynchronize();
            return;
        }

        Thread syncThread = new Thread( new Runnable() {
            @Override
            public void run() {
                RemoteProcessControlStateSynchronizer.this.awaitAsyncSynchronizeQuietWindow();
                RemoteProcessControlStateSynchronizer.this.runSynchronizeLoop( szReason );
            }
        }, "odin-control-state-sync" );
        syncThread.setDaemon( true );
        syncThread.start();
    }

    public boolean synchronizeBlocking( String szReason ) {
        if ( !this.startSynchronizing() ) {
            this.requestResynchronize();
            this.awaitSynchronizingFinished();
            return this.hasControlSession();
        }

        return this.runSynchronizeLoop( szReason );
    }

    protected boolean startSynchronizing() {
        this.mSyncLock.lock();
        try {
            if ( this.mbSynchronizing ) {
                this.mbResyncRequested = true;
                return false;
            }
            this.mbSynchronizing = true;
            return true;
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

    protected void requestResynchronize() {
        this.mSyncLock.lock();
        try {
            this.mbResyncRequested = true;
            this.mRetryCondition.signalAll();
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

    protected void awaitSynchronizingFinished() {
        this.mSyncLock.lock();
        try {
            while ( this.mbSynchronizing ) {
                this.mSyncFinishedCondition.await();
            }
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

    protected boolean hasControlSession() {
        return this.mszSessionGuid != null && !this.mszSessionGuid.isEmpty();
    }

    protected boolean runSynchronizeLoop( String szReason ) {
        int nFailureCount = 0;
        try {
            while ( true ) {
                this.mSyncLock.lock();
                try {
                    this.mbResyncRequested = false;
                }
                finally {
                    this.mSyncLock.unlock();
                }

                boolean bSynchronized = this.synchronizeOnce( szReason );
                if ( !bSynchronized ) {
                    this.awaitBeforeRetry( nFailureCount );
                    ++nFailureCount;
                    continue;
                }

                nFailureCount = 0;
                this.mSyncLock.lock();
                try {
                    if ( !this.mbResyncRequested ) {
                        return true;
                    }
                }
                finally {
                    this.mSyncLock.unlock();
                }
            }
        }
        finally {
            this.mSyncLock.lock();
            try {
                this.mbSynchronizing = false;
                this.mSyncFinishedCondition.signalAll();
            }
            finally {
                this.mSyncLock.unlock();
            }
        }
    }

    protected long retryDelayMillis( int nFailureCount ) {
        if ( nFailureCount <= 0 ) {
            return RetryDelayMillis0;
        }
        if ( nFailureCount == 1 ) {
            return RetryDelayMillis1;
        }
        return RetryDelayMillis2;
    }

    protected void awaitBeforeRetry( int nFailureCount ) {
        long nDelayMillis = this.retryDelayMillis( nFailureCount );
        this.mSyncLock.lock();
        try {
            if ( this.mbResyncRequested ) {
                return;
            }
            this.mRetryCondition.await( nDelayMillis, TimeUnit.MILLISECONDS );
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

    protected void awaitAsyncSynchronizeQuietWindow() {
        long nNanos = TimeUnit.MILLISECONDS.toNanos( AsyncSynchronizeQuietMillis );
        this.mSyncLock.lock();
        try {
            while ( nNanos > 0L ) {
                try {
                    nNanos = this.mRetryCondition.awaitNanos( nNanos );
                }
                catch ( InterruptedException e ) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

    protected boolean synchronizeOnce( String szReason ) {
        try {
            this.mClient.getLogger().info(
                    "[RemoteProcessControlSync] (Reason: `{}`, ClientId: `{}`) <Start>",
                    szReason,
                    this.mClient.getClientId()
            );
            RemoteProcessControlFrame muster = this.clientMusterFrame();
            RemoteProcessControlFrame ready = this.invokeControlFrame(
                    "musterClient",
                    muster.getClientId(),
                    muster.getFrameGuid(),
                    this.collectProcessMirrors()
            );
            if ( ready == null || ready.optFrameType() != RemoteProcessControlFrameType.ClientReady ) {
                this.warnUnexpectedFrame( "ClientMuster", ready, szReason );
                this.mClient.recoverControlPassiveChannels( szReason, null );
                return false;
            }

            this.mszSessionGuid = ready.getSessionGuid();
            this.mClient.getLogger().info( "[RemoteProcessControlSync] (Reason: `{}`, SessionGuid: `{}`) <Done>", szReason, this.mszSessionGuid );
            this.mClient.notifyControlStateSynchronized( szReason );
            return true;
        }
        catch ( Exception e ) {
            this.mClient.getLogger().warn( "[RemoteProcessControlSync] (Reason: `{}`) <Failure>", szReason, e );
            this.mClient.recoverControlPassiveChannels( szReason, e );
            return false;
        }
    }

    protected boolean exchangeAckFrame( RemoteProcessControlFrame request, String szStepName ) {
        RemoteProcessControlFrame ack = this.invokeControlFrame(
                "reportProcessMirror",
                request.getClientId(),
                request.getSessionGuid(),
                request.getFrameGuid(),
                request.getProcessMirror()
        );
        if ( ack != null && ack.optFrameType() == RemoteProcessControlFrameType.FrameAck ) {
            return true;
        }

        this.warnUnexpectedFrame( szStepName, ack, null );
        return false;
    }

    protected RemoteProcessControlFrame invokeControlFrame( String szMethodAddress, Object... args ) {
        try {
            Object response = this.mClient.duplexAppointClient().invokeInform(
                    ControlFrameIfaceAddressPrefix + szMethodAddress,
                    args,
                    ControlSyncRpcTimeoutMillis
            );
            if ( response == null ) {
                return null;
            }
            if ( response instanceof RemoteProcessControlFrame ) {
                return (RemoteProcessControlFrame) response;
            }
            throw new IllegalStateException( "Unexpected remote process control frame response type: " + response.getClass().getName() );
        }
        catch ( Exception e ) {
            throw new IllegalStateException( e );
        }
    }

    protected void warnUnexpectedFrame( String szStepName, RemoteProcessControlFrame frame, String szReason ) {
        String szFrameType = "null";
        String szMessage = "-";
        if ( frame != null ) {
            szFrameType = frame.getFrameType();
            szMessage = frame.getMessage();
        }
        if ( szReason == null ) {
            this.mClient.getLogger().warn(
                    "[RemoteProcessControlSync] [{}] (ClientId: `{}`, ResponseType: `{}`, Message: `{}`) <AckMissing>",
                    new Object[]{ szStepName, this.mClient.getClientId(), szFrameType, szMessage }
            );
            return;
        }

        this.mClient.getLogger().warn(
                "[RemoteProcessControlSync] [{}] (Reason: `{}`, ClientId: `{}`, ResponseType: `{}`, Message: `{}`) <Rejected>",
                new Object[]{ szStepName, szReason, this.mClient.getClientId(), szFrameType, szMessage }
        );
    }

    protected List<UProcessMirrorDTO> collectProcessMirrors() {
        List<UProcessMirrorDTO> processMirrors = new ArrayList<>();
        Collection<UProcess> processes = this.mClient.localProcessManager().fetchProcesses();
        for ( UProcess process : processes ) {
            if ( !this.shouldReportProcess( process ) ) {
                continue;
            }

            UProcessMirrorDTO processMirror = this.createProcessMirror( process );
            if ( processMirror == null ) {
                continue;
            }

            processMirrors.add( processMirror );
        }
        return processMirrors;
    }

    public void reportProcessMirror( UProcess process ) {
        if ( !this.shouldReportProcess( process ) ) {
            return;
        }

        UProcessMirrorDTO processMirror = this.createProcessMirror( process );
        if ( processMirror == null ) {
            return;
        }

        try {
            boolean bAccepted = this.exchangeAckFrame( this.processMirrorFrame( processMirror ), "ProcessMirror" );
            if ( !bAccepted ) {
                this.requestSynchronize( RemoteProcessControlSyncReasons.ProcessMirrorRejected );
            }
        }
        catch ( Exception e ) {
            this.mClient.getLogger().warn(
                    "[RemoteProcessControlSync] [ProcessMirror] (ClientId: `{}`, PID: `{}`) <Failure>",
                    new Object[]{ this.mClient.getClientId(), process.getPID(), e }
            );
            this.requestSynchronize( RemoteProcessControlSyncReasons.ProcessMirrorRejected );
        }
    }

    protected boolean shouldReportProcess( UProcess process ) {
        if ( process == null ) {
            return false;
        }

        if ( process == this.mClient.localProcessManager().getRootUProcess() ) {
            return false;
        }

        if ( !( process instanceof LocalUProcess ) ) {
            return false;
        }

        if ( process instanceof RemoteProcess ) {
            return false;
        }

        if ( process.getStatus().isTerminal() ) {
            return false;
        }

        if ( process.getExecutionImage() == null ) {
            return false;
        }

        return true;
    }

    public UProcessMirrorDTO createProcessMirror( UProcess process ) {
        UProcessMirrorDTO processMirror = new UProcessMirrorDTO();
        processMirror.setName( process.getName() );
        processMirror.setLocalPID( process.getLocalPID() );
        processMirror.setPID( process.getPID().toString() );

        GUID parentPID = process.actualParentPID();
        if ( parentPID == null ) {
            parentPID = process.getParentProcessId();
        }
        if ( parentPID != null ) {
            processMirror.setParentPID( parentPID.toString() );
        }

        if ( process.getStartupArguments() != null ) {
            processMirror.setStartupArguments( JSON.stringify( process.getStartupArguments() ) );
        }
        if ( process.getEnvironmentVariables() != null ) {
            processMirror.setEnvironmentVariables( JSON.stringify( process.getEnvironmentVariables() ) );
        }

        ExecutionImage image = process.getExecutionImage();
        String szImageAddress = image.getImageAddress();
        if ( szImageAddress == null && image.getResourceURI() != null ) {
            szImageAddress = image.getResourceURI().toString();
            processMirror.setImageAddressURI( true );
        }
        if ( szImageAddress == null || szImageAddress.isEmpty() ) {
            this.mClient.getLogger().warn( "[RemoteProcessControlSync] [ProcessMirror] (ClientId: `{}`, PID: `{}`) <MissingImageAddress>", this.mClient.getClientId(), process.getPID() );
            return null;
        }
        processMirror.setImageAddress( szImageAddress );
        return processMirror;
    }

    protected RemoteProcessControlFrame clientMusterFrame() {
        RemoteProcessControlFrame frame = this.baseFrame( RemoteProcessControlFrameType.ClientMuster );
        return frame;
    }

    protected RemoteProcessControlFrame processMirrorFrame( UProcessMirrorDTO processMirror ) {
        RemoteProcessControlFrame frame = this.baseFrame( RemoteProcessControlFrameType.ProcessMirror );
        frame.setProcessMirror( processMirror );
        return frame;
    }

    protected RemoteProcessControlFrame baseFrame( RemoteProcessControlFrameType frameType ) {
        RemoteProcessControlFrame frame = new RemoteProcessControlFrame();
        frame.setFrameGuid( this.nextGuidString() );
        frame.setClientId( this.mClient.getClientId() );
        frame.setSessionGuid( this.mszSessionGuid );
        frame.applyFrameType( frameType );
        return frame;
    }
}
