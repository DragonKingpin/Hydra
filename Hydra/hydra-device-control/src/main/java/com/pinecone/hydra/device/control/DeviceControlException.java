package com.pinecone.hydra.device.control;

public class DeviceControlException extends RuntimeException {

    public DeviceControlException() {
    }

    public DeviceControlException( String message ) {
        super( message );
    }

    public DeviceControlException( Throwable cause ) {
        super( cause );
    }

    public DeviceControlException( String message, Throwable cause ) {
        super( message, cause );
    }
}
