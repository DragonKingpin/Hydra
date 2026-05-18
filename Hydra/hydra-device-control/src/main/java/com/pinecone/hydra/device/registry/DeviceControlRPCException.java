package com.pinecone.hydra.device.registry;

public class DeviceControlRPCException extends DeviceControlException {

    public DeviceControlRPCException( Throwable cause ) {
        super( cause );
    }

    public DeviceControlRPCException( String message ) {
        super( message );
    }
}
