package com.pinecone.hydra.device.registry.client.transport;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.client.DeviceClientStateSynchronizedHandler;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientDeregisterResult;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;
import com.pinecone.hydra.device.registry.client.control.DeviceClientManipulationHandler;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;

public interface DeviceClientTransport extends AutoCloseable, Pinenut {

    void open() throws DeviceControlRPCException;

    DeviceClientRegisterResult registerDevice( DeviceRegisterInstruction instruction ) throws DeviceControlRPCException;

    DeviceClientDeregisterResult deregisterDevice( DeviceDeregisterInstruction instruction ) throws DeviceControlRPCException;

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

    @Override
    void close();
}
