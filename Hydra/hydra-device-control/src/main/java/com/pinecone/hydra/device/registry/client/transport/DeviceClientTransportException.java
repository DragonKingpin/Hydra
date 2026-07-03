package com.pinecone.hydra.device.registry.client.transport;

import com.pinecone.hydra.device.registry.DeviceControlRPCException;

public class DeviceClientTransportException extends DeviceControlRPCException {

    public DeviceClientTransportException( String message ) {
        super( message );
    }

    public DeviceClientTransportException( Throwable cause ) {
        super( cause );
    }
}
