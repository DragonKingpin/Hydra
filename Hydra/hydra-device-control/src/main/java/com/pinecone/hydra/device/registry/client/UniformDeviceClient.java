package com.pinecone.hydra.device.registry.client;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientDeregisterResult;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;
import com.pinecone.hydra.device.registry.client.control.DeviceClientManipulationHandler;
import com.pinecone.hydra.device.registry.client.transport.DeviceClientTransport;
import com.pinecone.hydra.device.registry.identity.DeviceClientIdentity;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;

public class UniformDeviceClient implements DeviceClient {

    protected final GUID deviceGuid;

    protected final DeviceClientTransport transport;

    protected GUID instanceGuid;

    public UniformDeviceClient( GUID deviceGuid, DeviceClientTransport transport ) {
        this.deviceGuid = deviceGuid;
        this.transport = transport;
    }

    @Override
    public GUID getDeviceGuid() {
        return this.deviceGuid;
    }

    @Override
    public long getClientId() {
        return DeviceClientIdentity.fromDeviceGuid( this.deviceGuid );
    }

    @Override
    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    @Override
    public void startDevice() throws DeviceControlRPCException {
        this.transport.open();
    }

    @Override
    public DeviceClientRegisterResult registerDevice() throws DeviceControlRPCException {
        DeviceRegisterInstruction instruction = new DeviceRegisterInstruction();
        instruction.setDeviceGuid( this.deviceGuid );
        return this.registerDevice( instruction );
    }

    @Override
    public DeviceClientRegisterResult registerDevice( DeviceRegisterInstruction instruction ) throws DeviceControlRPCException {
        DeviceClientRegisterResult result = this.transport.registerDevice( instruction );
        this.instanceGuid = result == null ? null : result.getInstanceGuid();
        return result;
    }

    @Override
    public DeviceClientDeregisterResult deregisterDevice( String reason ) throws DeviceControlRPCException {
        DeviceDeregisterInstruction instruction = new DeviceDeregisterInstruction();
        instruction.setInstanceGuid( this.instanceGuid );
        instruction.setReason( reason );
        return this.deregisterDevice( instruction );
    }

    @Override
    public DeviceClientDeregisterResult deregisterDevice( DeviceDeregisterInstruction instruction ) throws DeviceControlRPCException {
        DeviceClientDeregisterResult result = this.transport.deregisterDevice( instruction );
        this.instanceGuid = null;
        return result;
    }

    @Override
    public void terminateDevice() {
        this.transport.close();
    }

    @Override
    public void registerStateSynchronizedHandler( DeviceClientStateSynchronizedHandler handler ) {
        this.transport.registerStateSynchronizedHandler( handler );
    }

    @Override
    public void deregisterStateSynchronizedHandler( DeviceClientStateSynchronizedHandler handler ) {
        this.transport.deregisterStateSynchronizedHandler( handler );
    }

    @Override
    public void registerManipulationHandler( DeviceClientManipulationHandler handler ) {
        this.transport.registerManipulationHandler( handler );
    }

    @Override
    public void deregisterManipulationHandler( DeviceClientManipulationHandler handler ) {
        this.transport.deregisterManipulationHandler( handler );
    }

    @Override
    public void requestControlStateSynchronization( String reason ) {
        this.transport.requestControlStateSynchronization( reason );
    }
}
