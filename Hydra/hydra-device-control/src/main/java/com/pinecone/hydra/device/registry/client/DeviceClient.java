package com.pinecone.hydra.device.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientDeregisterResult;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;
import com.pinecone.hydra.device.registry.client.control.DeviceClientManipulationHandler;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;

public interface DeviceClient extends Pinenut {

    GUID getDeviceGuid();

    long getClientId();

    GUID getInstanceGuid();

    void startDevice() throws DeviceControlRPCException;

    DeviceClientRegisterResult registerDevice() throws DeviceControlRPCException;

    DeviceClientRegisterResult registerDevice( DeviceRegisterInstruction instruction ) throws DeviceControlRPCException;

    DeviceClientDeregisterResult deregisterDevice( String reason ) throws DeviceControlRPCException;

    DeviceClientDeregisterResult deregisterDevice( DeviceDeregisterInstruction instruction ) throws DeviceControlRPCException;

    void terminateDevice();

    default void registerStateSynchronizedHandler( DeviceClientStateSynchronizedHandler handler ) {
    }

    default void deregisterStateSynchronizedHandler( DeviceClientStateSynchronizedHandler handler ) {
    }

    default void registerManipulationHandler( DeviceClientManipulationHandler handler ) {
    }

    default void deregisterManipulationHandler( DeviceClientManipulationHandler handler ) {
    }

    default void requestControlStateSynchronization( String reason ) {
    }
}
