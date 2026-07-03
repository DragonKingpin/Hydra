package com.acorn.redqueen.service.conduct;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.client.ServiceClient;
import com.pinecone.hydra.service.registry.client.ServiceClientStateSynchronizedHandler;
import com.pinecone.hydra.service.registry.client.control.ServiceClientManipulationHandler;
import com.pinecone.hydra.service.registry.instruction.ServiceShutdownInstruction;
import com.pinecone.hydra.service.registry.instruction.ServiceDeregisterInstruction;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;

public class RedCollectiveServiceLegionary implements CollectiveServiceLegionary {

    protected static final long RejoinRetryDelayMillis0 = 500L;

    protected static final long RejoinRetryDelayMillis1 = 1000L;

    protected static final long RejoinRetryDelayMillis2 = 2000L;

    protected String mszName;

    protected ServiceClient mServiceClient;

    protected ServiceLegionaryJoinRequest mJoinRequest;

    protected ServiceLegionaryJoinFactory mJoinFactory;

    protected ServiceClientStateSynchronizedHandler mServiceClientStateSynchronizedHandler;

    protected ServiceClientManipulationHandler mServiceClientManipulationHandler;

    protected volatile GUID mServiceGuid;

    protected volatile GUID mInstanceGuid;

    protected volatile ServiceLegionaryState mState;

    protected ReentrantLock mRegimentRejoinLock;

    protected Condition mRegimentRejoinCondition;

    protected boolean mbRegimentRejoining;

    protected boolean mbRegimentRejoinRequested;

    protected boolean mbTerminated;

    protected String mszRegimentRejoinReason;

    protected Logger mLogger;

    public RedCollectiveServiceLegionary(
            String szName,
            ServiceClient serviceClient,
            ServiceLegionaryJoinRequest joinRequest
    ) {
        this.mszName = szName;
        this.mServiceClient = serviceClient;
        this.mJoinRequest = joinRequest;
        this.mJoinFactory = new ServiceLegionaryJoinFactory();
        this.mServiceGuid = joinRequest == null ? null : joinRequest.getServiceGuid();
        this.mState = ServiceLegionaryState.New;
        this.mRegimentRejoinLock = new ReentrantLock();
        this.mRegimentRejoinCondition = this.mRegimentRejoinLock.newCondition();
        this.mLogger = LoggerFactory.getLogger( this.getClass() );
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public long getClientId() {
        if ( this.mServiceClient == null ) {
            return 0L;
        }
        return this.mServiceClient.getClientId();
    }

    @Override
    public GUID getServiceGuid() {
        return this.mServiceGuid;
    }

    @Override
    public GUID getInstanceGuid() {
        return this.mInstanceGuid;
    }

    public ServiceLegionaryState getState() {
        return this.mState;
    }

    public ServiceClient serviceClient() {
        return this.mServiceClient;
    }

    public ServiceLegionaryJoinRequest joinRequest() {
        return this.mJoinRequest;
    }

    @Override
    public void startService() throws ServiceLegionaryException {
        try {
            this.assertServiceClientReady();
            this.registerServiceClientManipulationHandler();
            this.mState = ServiceLegionaryState.Starting;
            this.mServiceClient.startService();
            this.mState = ServiceLegionaryState.ControlReady;
            this.registerServiceClientStateSynchronizedHandler();
        }
        catch ( ServiceControlRPCException e ) {
            this.mState = ServiceLegionaryState.Offline;
            throw new ServiceLegionaryException( e );
        }
    }

    protected void assertServiceClientReady() throws ServiceLegionaryException {
        if ( this.mServiceClient == null ) {
            throw new ServiceLegionaryException( "Hydra service client is null." );
        }
    }

    protected void registerServiceClientStateSynchronizedHandler() {
        if ( this.mServiceClientStateSynchronizedHandler != null ) {
            return;
        }

        this.mServiceClientStateSynchronizedHandler = new ServiceClientStateSynchronizedHandler() {
            @Override
            public void afterServiceClientStateSynchronized( String szReason ) {
                RedCollectiveServiceLegionary.this.mState = ServiceLegionaryState.Offline;
                RedCollectiveServiceLegionary.this.requestRejoinRegiment( szReason );
            }
        };
        this.mServiceClient.registerStateSynchronizedHandler( this.mServiceClientStateSynchronizedHandler );
    }

    protected void registerServiceClientManipulationHandler() {
        if ( this.mServiceClientManipulationHandler != null ) {
            return;
        }

        this.mServiceClientManipulationHandler = new ServiceClientManipulationHandler() {
            @Override
            public void shutdownService( ServiceShutdownInstruction instruction ) {
                RedCollectiveServiceLegionary.this.requestPassiveShutdownService( instruction );
            }
        };
        this.mServiceClient.registerManipulationHandler( this.mServiceClientManipulationHandler );
    }

    protected void requestPassiveShutdownService( ServiceShutdownInstruction instruction ) {
        if ( instruction == null ) {
            return;
        }
        if ( this.mInstanceGuid != null && instruction.getInstanceGuid() != null && !this.mInstanceGuid.equals( instruction.getInstanceGuid() ) ) {
            this.mLogger.warn(
                    "[RedQueenServiceLegionary] [PassiveShutdown] (Name: `{}`, ClientId: `{}`, CurrentInstance: `{}`, RequestedInstance: `{}`) <Ignored>",
                    this.mszName,
                    this.getClientId(),
                    this.mInstanceGuid,
                    instruction.getInstanceGuid()
            );
            return;
        }

        Thread shutdownThread = new Thread( new Runnable() {
            @Override
            public void run() {
                RedCollectiveServiceLegionary.this.terminateService( instruction.getReason() );
            }
        }, "redqueen-service-passive-shutdown" );
        shutdownThread.setDaemon( true );
        shutdownThread.start();
    }

    @Override
    public ServiceLegionaryJoinResponse joinRegiment() throws ServiceLegionaryException {
        try {
            this.assertServiceClientReady();
            this.assertJoinRequestReady();
            this.mState = ServiceLegionaryState.Joining;

            ServiceLegionaryJoinResponse joinResponse = this.mJoinFactory.fromRegisterResult(
                    this.mServiceClient.lifecycle().register(
                            this.mJoinFactory.toRegisterInstruction( this.getClientId(), this.mInstanceGuid, this.mJoinRequest )
                    )
            );
            this.mServiceGuid = joinResponse.getServiceGuid();
            this.mInstanceGuid = joinResponse.getInstanceGuid();
            this.mState = ServiceLegionaryState.Online;

            this.mLogger.info(
                    "[RedQueenServiceLegionary] [JoinRegiment] (Name: `{}`, ClientId: `{}`, Service: `{}`, Instance: `{}`) <Done>",
                    this.mszName,
                    this.getClientId(),
                    this.mServiceGuid,
                    this.mInstanceGuid
            );
            return joinResponse;
        }
        catch ( ServiceClientTransportException e ) {
            this.mState = ServiceLegionaryState.Offline;
            throw new ServiceLegionaryException( e );
        }
    }

    protected void assertJoinRequestReady() throws ServiceLegionaryException {
        if ( this.mJoinRequest == null ) {
            throw new ServiceLegionaryException( "RedQueen service legionary join request is null." );
        }
        if ( this.mJoinRequest.getServiceGuid() == null ) {
            throw new ServiceLegionaryException( "RedQueen service legionary service guid is null." );
        }
    }

    @Override
    public void requestRejoinRegiment( String szReason ) {
        if ( this.mbTerminated ) {
            return;
        }
        if ( this.mJoinRequest == null ) {
            return;
        }

        this.mRegimentRejoinLock.lock();
        try {
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
                RedCollectiveServiceLegionary.this.runRejoinRegimentLoop();
            }
        }, "redqueen-service-regiment-rejoin" );
        rejoinThread.setDaemon( true );
        rejoinThread.start();
    }

    protected void runRejoinRegimentLoop() {
        int nFailureCount = 0;
        try {
            while ( !this.mbTerminated ) {
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
                if ( this.mbRegimentRejoinRequested && !this.mbTerminated ) {
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
            this.mState = ServiceLegionaryState.Rejoining;
            this.joinRegiment();
            this.mLogger.info(
                    "[RedQueenServiceLegionary] [RejoinRegiment] (Reason: `{}`, Name: `{}`, ClientId: `{}`) <Done>",
                    szReason,
                    this.mszName,
                    this.getClientId()
            );
            return true;
        }
        catch ( ServiceLegionaryException e ) {
            this.mState = ServiceLegionaryState.Offline;
            this.mLogger.warn(
                    "[RedQueenServiceLegionary] [RejoinRegiment] (Reason: `{}`, Name: `{}`, ClientId: `{}`) <Failure>",
                    szReason,
                    this.mszName,
                    this.getClientId(),
                    e
            );
            return false;
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
    public void deregister( String szReason ) throws ServiceLegionaryException {
        if ( this.mInstanceGuid == null ) {
            this.mState = ServiceLegionaryState.Offline;
            return;
        }

        try {
            ServiceDeregisterInstruction command = new ServiceDeregisterInstruction();
            command.setInstanceGuid( this.mInstanceGuid );
            command.setReason( szReason );
            this.mServiceClient.lifecycle().deregister( command );
            this.mInstanceGuid = null;
            this.mState = ServiceLegionaryState.Offline;
        }
        catch ( ServiceClientTransportException e ) {
            throw new ServiceLegionaryException( e );
        }
    }

    @Override
    public void terminateService() {
        this.terminateService( "Terminated" );
    }

    protected void terminateService( String szReason ) {
        this.mbTerminated = true;
        this.mRegimentRejoinLock.lock();
        try {
            this.mbRegimentRejoinRequested = false;
            this.mRegimentRejoinCondition.signalAll();
        }
        finally {
            this.mRegimentRejoinLock.unlock();
        }

        try {
            this.deregister( szReason );
        }
        catch ( Exception e ) {
            this.mLogger.warn(
                    "[RedQueenServiceLegionary] [Terminate] (Name: `{}`, ClientId: `{}`) <DeregisterFailure>",
                    this.mszName,
                    this.getClientId(),
                    e
            );
        }

        if ( this.mServiceClientStateSynchronizedHandler != null && this.mServiceClient != null ) {
            this.mServiceClient.deregisterStateSynchronizedHandler( this.mServiceClientStateSynchronizedHandler );
            this.mServiceClientStateSynchronizedHandler = null;
        }
        if ( this.mServiceClientManipulationHandler != null && this.mServiceClient != null ) {
            this.mServiceClient.deregisterManipulationHandler( this.mServiceClientManipulationHandler );
            this.mServiceClientManipulationHandler = null;
        }
        if ( this.mServiceClient != null ) {
            this.mServiceClient.terminateService();
        }
        this.mState = ServiceLegionaryState.Terminated;
    }

}
