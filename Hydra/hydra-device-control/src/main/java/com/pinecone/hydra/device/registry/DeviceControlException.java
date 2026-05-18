package com.pinecone.hydra.device.registry;

import com.pinecone.framework.system.prototype.Pinenut;

public class DeviceControlException extends RuntimeException implements Pinenut {

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
