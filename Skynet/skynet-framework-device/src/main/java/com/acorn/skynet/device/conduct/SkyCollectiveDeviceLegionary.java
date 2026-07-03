package com.acorn.skynet.device.conduct;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.client.DeviceClient;
import com.pinecone.hydra.device.registry.client.DeviceClientStateSynchronizedHandler;
import com.pinecone.hydra.device.registry.client.control.DeviceClientManipulationHandler;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceShutdownInstruction;

public class SkyCollectiveDeviceLegionary implements CollectiveDeviceLegionary {

    protected static final long RejoinRetryDelayMillis0 = 500L;

    protected static final long RejoinRetryDelayMillis1 = 1000L;

    protected static final long RejoinRetryDelayMillis2 = 2000L;

    protected final String name;

    protected final DeviceClient deviceClient;

    protected final DeviceLegionaryJoinRequest joinRequest;

    protected DeviceClientStateSynchronizedHandler stateSynchronizedHandler;

    protected DeviceClientManipulationHandler manipulationHandler;

    protected volatile GUID deviceGuid;

    protected volatile GUID instanceGuid;

    protected volatile DeviceLegionaryState state;

    protected final ReentrantLock regimentRejoinLock;

    protected final Condition regimentRejoinCondition;

    protected boolean regimentRejoining;

    protected boolean regimentRejoinRequested;

    protected boolean terminated;

    protected String regimentRejoinReason;

    protected final Logger logger;

    public SkyCollectiveDeviceLegionary(
            String name,
            DeviceClient deviceClient,
            DeviceLegionaryJoinRequest joinRequest
    ) {
        this.name = name;
        this.deviceClient = deviceClient;
        this.joinRequest = joinRequest;
        this.deviceGuid = joinRequest == null ? null : joinRequest.getDeviceGuid();
        this.state = DeviceLegionaryState.New;
        this.regimentRejoinLock = new ReentrantLock();
        this.regimentRejoinCondition = this.regimentRejoinLock.newCondition();
        this.logger = LoggerFactory.getLogger( this.getClass() );
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getClientId() {
        if ( this.deviceClient == null ) {
            return 0L;
        }
        return this.deviceClient.getClientId();
    }

    @Override
    public GUID getDeviceGuid() {
        return this.deviceGuid;
    }

    @Override
    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    @Override
    public DeviceLegionaryState getState() {
        return this.state;
    }

    public DeviceClient deviceClient() {
        return this.deviceClient;
    }

    public DeviceLegionaryJoinRequest joinRequest() {
        return this.joinRequest;
    }

    @Override
    public void startDevice() throws DeviceLegionaryException {
        try {
            this.assertDeviceClientReady();
            this.registerDeviceClientManipulationHandler();
            this.state = DeviceLegionaryState.Starting;
            this.deviceClient.startDevice();
            this.state = DeviceLegionaryState.ControlReady;
            this.registerDeviceClientStateSynchronizedHandler();
        }
        catch ( DeviceControlRPCException e ) {
            this.state = DeviceLegionaryState.Offline;
            throw new DeviceLegionaryException( e );
        }
    }

    protected void assertDeviceClientReady() throws DeviceLegionaryException {
        if ( this.deviceClient == null ) {
            throw new DeviceLegionaryException( "Hydra device client is null." );
        }
    }

    protected void registerDeviceClientStateSynchronizedHandler() {
        if ( this.stateSynchronizedHandler != null ) {
            return;
        }

        this.stateSynchronizedHandler = new DeviceClientStateSynchronizedHandler() {
            @Override
            public void afterDeviceClientStateSynchronized( String reason ) {
                SkyCollectiveDeviceLegionary.this.requestRejoinRegiment( reason );
            }
        };
        this.deviceClient.registerStateSynchronizedHandler( this.stateSynchronizedHandler );
    }

    protected void registerDeviceClientManipulationHandler() {
        if ( this.manipulationHandler != null ) {
            return;
        }

        this.manipulationHandler = new DeviceClientManipulationHandler() {
            @Override
            public void shutdownDevice( DeviceShutdownInstruction instruction ) {
                SkyCollectiveDeviceLegionary.this.requestPassiveShutdownDevice( instruction );
            }
        };
        this.deviceClient.registerManipulationHandler( this.manipulationHandler );
    }

    protected void requestPassiveShutdownDevice( DeviceShutdownInstruction instruction ) {
        if ( instruction == null ) {
            return;
        }
        if ( this.instanceGuid != null && instruction.getInstanceGuid() != null && !this.instanceGuid.equals( instruction.getInstanceGuid() ) ) {
            this.logger.warn(
                    "[SkynetDeviceLegionary] [PassiveShutdown] (Name: `{}`, ClientId: `{}`, CurrentInstance: `{}`, RequestedInstance: `{}`) <Ignored>",
                    this.name,
                    this.getClientId(),
                    this.instanceGuid,
                    instruction.getInstanceGuid()
            );
            return;
        }

        Thread shutdownThread = new Thread( new Runnable() {
            @Override
            public void run() {
                SkyCollectiveDeviceLegionary.this.terminateDevice( instruction.getReason() );
            }
        }, "skynet-device-passive-shutdown" );
        shutdownThread.setDaemon( true );
        shutdownThread.start();
    }

    @Override
    public DeviceLegionaryJoinResponse joinRegiment() throws DeviceLegionaryException {
        try {
            this.assertDeviceClientReady();
            this.assertJoinRequestReady();
            this.state = DeviceLegionaryState.Joining;

            DeviceRegisterInstruction instruction = this.joinRequest.toDeviceRegisterInstruction( this.getClientId() );
            instruction.setInstanceGuid( this.instanceGuid );
            DeviceClientRegisterResult response = this.deviceClient.registerDevice( instruction );
            DeviceLegionaryJoinResponse joinResponse = DeviceLegionaryJoinResponse.from( response );
            this.deviceGuid = joinResponse.getDeviceGuid();
            this.instanceGuid = joinResponse.getInstanceGuid();
            this.state = DeviceLegionaryState.Online;

            this.logger.info(
                    "[SkynetDeviceLegionary] [JoinRegiment] (Name: `{}`, ClientId: `{}`, Device: `{}`, Instance: `{}`) <Done>",
                    this.name,
                    this.getClientId(),
                    this.deviceGuid,
                    this.instanceGuid
            );
            return joinResponse;
        }
        catch ( DeviceControlRPCException e ) {
            this.state = DeviceLegionaryState.Offline;
            throw new DeviceLegionaryException( e );
        }
    }

    protected void assertJoinRequestReady() throws DeviceLegionaryException {
        if ( this.joinRequest == null ) {
            throw new DeviceLegionaryException( "Skynet device legionary join request is null." );
        }
        if (
                this.joinRequest.getDeviceGuid() == null
                        && this.isBlank( this.joinRequest.getDeviceGuidText() )
                        && this.isBlank( this.joinRequest.getDevicePath() )
        ) {
            throw new DeviceLegionaryException( "Skynet device legionary device identity is null." );
        }
    }

    @Override
    public void requestRejoinRegiment( String reason ) {
        if ( this.terminated || this.joinRequest == null ) {
            return;
        }

        this.regimentRejoinLock.lock();
        try {
            this.regimentRejoinRequested = true;
            this.regimentRejoinReason = reason;
            this.regimentRejoinCondition.signalAll();
            if ( this.regimentRejoining ) {
                return;
            }
            this.regimentRejoining = true;
        }
        finally {
            this.regimentRejoinLock.unlock();
        }

        Thread rejoinThread = new Thread( new Runnable() {
            @Override
            public void run() {
                SkyCollectiveDeviceLegionary.this.runRejoinRegimentLoop();
            }
        }, "skynet-device-regiment-rejoin" );
        rejoinThread.setDaemon( true );
        rejoinThread.start();
    }

    protected void runRejoinRegimentLoop() {
        int failureCount = 0;
        try {
            while ( !this.terminated ) {
                String reason = this.consumeRejoinReason();
                if ( reason == null ) {
                    return;
                }

                if ( this.rejoinRegimentOnce( reason ) ) {
                    failureCount = 0;
                    continue;
                }

                this.awaitBeforeRejoinRetry( failureCount );
                ++failureCount;
                this.requestRejoinRegiment( reason );
            }
        }
        finally {
            this.regimentRejoinLock.lock();
            try {
                this.regimentRejoining = false;
                if ( this.regimentRejoinRequested && !this.terminated ) {
                    this.requestRejoinRegiment( this.regimentRejoinReason );
                }
            }
            finally {
                this.regimentRejoinLock.unlock();
            }
        }
    }

    protected String consumeRejoinReason() {
        this.regimentRejoinLock.lock();
        try {
            if ( !this.regimentRejoinRequested ) {
                return null;
            }

            this.regimentRejoinRequested = false;
            return this.regimentRejoinReason;
        }
        finally {
            this.regimentRejoinLock.unlock();
        }
    }

    protected boolean rejoinRegimentOnce( String reason ) {
        try {
            this.state = DeviceLegionaryState.Rejoining;
            this.joinRegiment();
            this.logger.info(
                    "[SkynetDeviceLegionary] [RejoinRegiment] (Reason: `{}`, Name: `{}`, ClientId: `{}`) <Done>",
                    reason,
                    this.name,
                    this.getClientId()
            );
            return true;
        }
        catch ( DeviceLegionaryException e ) {
            this.state = DeviceLegionaryState.Offline;
            this.logger.warn(
                    "[SkynetDeviceLegionary] [RejoinRegiment] (Reason: `{}`, Name: `{}`, ClientId: `{}`) <Failure>",
                    reason,
                    this.name,
                    this.getClientId(),
                    e
            );
            return false;
        }
    }

    protected long rejoinRetryDelayMillis( int failureCount ) {
        if ( failureCount <= 0 ) {
            return RejoinRetryDelayMillis0;
        }
        if ( failureCount == 1 ) {
            return RejoinRetryDelayMillis1;
        }
        return RejoinRetryDelayMillis2;
    }

    protected void awaitBeforeRejoinRetry( int failureCount ) {
        long delayMillis = this.rejoinRetryDelayMillis( failureCount );
        this.regimentRejoinLock.lock();
        try {
            if ( this.regimentRejoinRequested ) {
                return;
            }
            this.regimentRejoinCondition.await( delayMillis, TimeUnit.MILLISECONDS );
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        finally {
            this.regimentRejoinLock.unlock();
        }
    }

    @Override
    public void deregister( String reason ) throws DeviceLegionaryException {
        if ( this.instanceGuid == null ) {
            this.state = DeviceLegionaryState.Offline;
            return;
        }

        try {
            DeviceDeregisterInstruction instruction = new DeviceDeregisterInstruction();
            instruction.setInstanceGuid( this.instanceGuid );
            instruction.setReason( reason );
            this.deviceClient.deregisterDevice( instruction );
            this.instanceGuid = null;
            this.state = DeviceLegionaryState.Offline;
        }
        catch ( DeviceControlRPCException e ) {
            throw new DeviceLegionaryException( e );
        }
    }

    @Override
    public void terminateDevice() {
        this.terminateDevice( "Terminated" );
    }

    protected void terminateDevice( String reason ) {
        this.terminated = true;
        this.regimentRejoinLock.lock();
        try {
            this.regimentRejoinRequested = false;
            this.regimentRejoinCondition.signalAll();
        }
        finally {
            this.regimentRejoinLock.unlock();
        }

        try {
            this.deregister( reason );
        }
        catch ( Exception e ) {
            this.logger.warn(
                    "[SkynetDeviceLegionary] [Terminate] (Name: `{}`, ClientId: `{}`) <DeregisterFailure>",
                    this.name,
                    this.getClientId(),
                    e
            );
        }

        if ( this.stateSynchronizedHandler != null && this.deviceClient != null ) {
            this.deviceClient.deregisterStateSynchronizedHandler( this.stateSynchronizedHandler );
            this.stateSynchronizedHandler = null;
        }
        if ( this.manipulationHandler != null && this.deviceClient != null ) {
            this.deviceClient.deregisterManipulationHandler( this.manipulationHandler );
            this.manipulationHandler = null;
        }
        if ( this.deviceClient != null ) {
            this.deviceClient.terminateDevice();
        }
        this.state = DeviceLegionaryState.Terminated;
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
