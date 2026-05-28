package com.walnut.odin.conduct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.construction.Postpone;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UniformProcessManager;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.walnut.odin.conduct.entity.RegimentJoinRequest;
import com.walnut.odin.conduct.entity.RegimentJoinResponse;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.client.RavenRemoteProcessManagerClient;
import com.walnut.odin.proc.client.RemoteProcessManagerClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RavenCollectiveTaskLegionary implements CollectiveTaskLegionary {

    protected static final long                  RejoinRetryDelayMillis0 = 500;

    protected static final long                  RejoinRetryDelayMillis1 = 1000;

    protected static final long                  RejoinRetryDelayMillis2 = 2000;

    protected String                           mszNodeName;
    protected RemoteProcessManagerClient       mRemoteProcessManagerClient;
    protected ProcessManager                   mLocalProcessManager;
    protected ProcessorLifecycleIface          mProcessLifecycleIface;

    protected RavenRemoteProcessManagerClient.ControlStateSynchronizedHandler mControlStateSynchronizedHandler;

    protected ReentrantLock                   mRegimentRejoinLock;
    protected Condition                       mRegimentRejoinCondition;
    protected boolean                         mbRegimentRejoining;
    protected boolean                         mbRegimentRejoinRequested;
    protected boolean                         mbRegimentRejected;
    protected String                          mszRegimentRejoinReason;
    protected String                          mszRegimentRejectedReason;

    protected Logger                           mLogger;

    protected RavenCollectiveTaskLegionary( ProcessManager processManager, @Postpone RemoteProcessManagerClient pmClient, String szNodeName ) {
        this.mszNodeName                 = szNodeName;
        this.mLocalProcessManager        = processManager;
        this.mRemoteProcessManagerClient = pmClient;
        this.mLogger                     = LoggerFactory.getLogger( this.getClass() );
        this.mRegimentRejoinLock         = new ReentrantLock();
        this.mRegimentRejoinCondition    = this.mRegimentRejoinLock.newCondition();
    }

    public RavenCollectiveTaskLegionary( String szNodeName, ProcessManager processManager, RemoteProcessManagerClient pmClient ) {
        this( processManager, pmClient, szNodeName );
    }

    public RavenCollectiveTaskLegionary( String szNodeName, Processum superiorProcess, UlfClient rpcClient ) {
        this(
                new UniformProcessManager(
                        superiorProcess, null, ( szNodeName + "-process-manager" ).toLowerCase(), "", null
                ),
                null,
                szNodeName
        );

        this.mRemoteProcessManagerClient = new RavenRemoteProcessManagerClient( this.mLocalProcessManager, rpcClient );
    }


    @Override
    public String getName() {
        return this.mszNodeName;
    }

    @Override
    public long getClientId() {
        return this.mRemoteProcessManagerClient.getClientId();
    }

    @Override
    public ProcessManager processManager() {
        return this.mLocalProcessManager;
    }

    @Override
    public RemoteProcessManagerClient remoteProcessManagerClient() {
        return this.mRemoteProcessManagerClient;
    }

    @Override
    public void startService () throws RemoteProcessServiceRPCException {
        this.mRemoteProcessManagerClient.startService();

        DuplexAppointClient duplexAppointClient = this.mRemoteProcessManagerClient.duplexAppointClient();
        duplexAppointClient.compile( ProcessorLifecycleIface.class,false );
        this.mProcessLifecycleIface = duplexAppointClient.getIface( ProcessorLifecycleIface.class );
        this.registerControlStateSynchronizedHandler();
    }

    protected void registerControlStateSynchronizedHandler() {
        if ( this.mControlStateSynchronizedHandler != null ) {
            return;
        }
        if ( !( this.mRemoteProcessManagerClient instanceof RavenRemoteProcessManagerClient ) ) {
            return;
        }

        this.mControlStateSynchronizedHandler = new RavenRemoteProcessManagerClient.ControlStateSynchronizedHandler() {
            @Override
            public void afterControlStateSynchronized( String szReason ) {
                RavenCollectiveTaskLegionary.this.requestRejoinRegiment( szReason );
            }
        };
        ( (RavenRemoteProcessManagerClient)this.mRemoteProcessManagerClient ).registerControlStateSynchronizedHandler( this.mControlStateSynchronizedHandler );
    }

    protected void requestRejoinRegiment( String szReason ) {
        if ( this.mProcessLifecycleIface == null ) {
            return;
        }

        this.mRegimentRejoinLock.lock();
        try {
            if ( this.mbRegimentRejected ) {
                return;
            }
            this.mbRegimentRejoinRequested = true;
            this.mszRegimentRejoinReason = szReason;
            this.mRegimentRejoinCondition.signalAll();
            if ( this.mbRegimentRejoining ) {
                return;
            }
            this.mbRegimentRejoining = true;
        }
        finally {
            this.mRegimentRejoinLock.unlock();
        }

        Thread rejoinThread = new Thread( new Runnable() {
            @Override
            public void run() {
                RavenCollectiveTaskLegionary.this.runRejoinRegimentLoop();
            }
        }, "odin-regiment-rejoin" );
        rejoinThread.setDaemon( true );
        rejoinThread.start();
    }

    protected void runRejoinRegimentLoop() {
        int nFailureCount = 0;
        try {
            while ( true ) {
                String szReason = this.consumeRejoinReason();
                if ( szReason == null ) {
                    return;
                }

                if ( this.rejoinRegimentOnce( szReason ) ) {
                    nFailureCount = 0;
                    continue;
                }

                this.awaitBeforeRejoinRetry( nFailureCount );
                ++nFailureCount;
                this.requestRejoinRegiment( szReason );
            }
        }
        finally {
            this.mRegimentRejoinLock.lock();
            try {
                this.mbRegimentRejoining = false;
                if ( this.mbRegimentRejected ) {
                    this.mbRegimentRejoinRequested = false;
                    return;
                }
                if ( this.mbRegimentRejoinRequested ) {
                    this.requestRejoinRegiment( this.mszRegimentRejoinReason );
                }
            }
            finally {
                this.mRegimentRejoinLock.unlock();
            }
        }
    }

    protected String consumeRejoinReason() {
        this.mRegimentRejoinLock.lock();
        try {
            if ( !this.mbRegimentRejoinRequested ) {
                return null;
            }

            this.mbRegimentRejoinRequested = false;
            return this.mszRegimentRejoinReason;
        }
        finally {
            this.mRegimentRejoinLock.unlock();
        }
    }

    protected boolean rejoinRegimentOnce( String szReason ) {
        try {
            this.joinRegiment();
            this.mLogger.info(
                    "[NewProcessorRegister] (Reason: `{}`, name:`{}`, clientId:`{}`) <Rejoined>",
                    szReason,
                    this.mszNodeName,
                    this.getClientId()
            );
            return true;
        }
        catch ( RegimentException e ) {
            if ( RegimentJoinInstructs.isApoptosis( e.getMessage() ) ) {
                this.markRegimentRejected( e.getMessage() );
                this.mLogger.error(
                        "[NewProcessorRegister] (Reason: `{}`, name:`{}`, clientId:`{}`) <RejectedFatal>",
                        szReason,
                        this.mszNodeName,
                        this.getClientId(),
                        e
                );
                this.terminateAfterRegimentRejected();
                return true;
            }
            this.mLogger.warn(
                    "[NewProcessorRegister] (Reason: `{}`, name:`{}`, clientId:`{}`) <RejoinFailure>",
                    szReason,
                    this.mszNodeName,
                    this.getClientId(),
                    e
            );
            return false;
        }
    }

    protected void markRegimentRejected( String szReason ) {
        this.mRegimentRejoinLock.lock();
        try {
            this.mbRegimentRejected = true;
            this.mszRegimentRejectedReason = szReason;
            this.mbRegimentRejoinRequested = false;
            this.mRegimentRejoinCondition.signalAll();
        }
        finally {
            this.mRegimentRejoinLock.unlock();
        }
    }

    protected void terminateAfterRegimentRejected() {
        try {
            this.mRemoteProcessManagerClient.terminateService();
        }
        catch ( Exception e ) {
            this.mLogger.warn(
                    "[NewProcessorRegister] (name:`{}`, clientId:`{}`, reason:`{}`) <RejectedTerminationFailure>",
                    this.mszNodeName,
                    this.getClientId(),
                    this.mszRegimentRejectedReason,
                    e
            );
        }
    }

    protected long rejoinRetryDelayMillis( int nFailureCount ) {
        if ( nFailureCount <= 0 ) {
            return RejoinRetryDelayMillis0;
        }
        if ( nFailureCount == 1 ) {
            return RejoinRetryDelayMillis1;
        }
        return RejoinRetryDelayMillis2;
    }

    protected void awaitBeforeRejoinRetry( int nFailureCount ) {
        long nDelayMillis = this.rejoinRetryDelayMillis( nFailureCount );
        this.mRegimentRejoinLock.lock();
        try {
            if ( this.mbRegimentRejoinRequested ) {
                return;
            }
            this.mRegimentRejoinCondition.await( nDelayMillis, TimeUnit.MILLISECONDS );
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        finally {
            this.mRegimentRejoinLock.unlock();
        }
    }

    @Override
    public RegimentJoinResponse joinRegiment() throws RegimentException {
        RegimentJoinRequest request = new RegimentJoinRequest();
        request.setClientId( this.mRemoteProcessManagerClient.getClientId() );
        request.setNodeName( this.mszNodeName );
        RegimentJoinResponse response = this.mProcessLifecycleIface.joinRegiment( request );
        if ( response == null ) {
            throw new RegimentException( "ProcessorLifecycleIface.joinRegiment returned null; controller may not be registered or iface may not be compiled." );
        }
        else if ( StringUtils.isNoneEmpty( response.getErrorMsg() ) ) {
            throw new RegimentException( response.getErrorMsg() );
        }

        this.mLogger.info(
                "[NewProcessorRegister] " +
                "( name:`{}`, clientId:`{}`, clusterPath:`{}`, priority:`{}`, queueMaxCapacity:`{}`, runtimeCapacity:`{}` ) " +
                "<RegimentServerAck>",

                response.getName(), response.getControlClientId(), response.getClusterPath(), response.getPriority(),
                response.getQueueMaxCapacity(), response.getQueueRuntimeInstanceCapacity()
        );
        return response;
    }

}
