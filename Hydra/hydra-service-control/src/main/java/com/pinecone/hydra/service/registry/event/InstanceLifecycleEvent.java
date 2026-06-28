package com.pinecone.hydra.service.registry.event;

public enum InstanceLifecycleEvent {
    Created      ( 0x00, "Created" ),

    Registered   ( 0x01, "Registered" ),

    Detached     ( 0x02, "Detached" ),

    Recovered    ( 0x03, "Recovered" ),

    Deregistered ( 0x04, "Deregistered" ),

    Offline      ( 0x05, "Offline" ),

    Expired      ( 0x06, "Expired" ),

    Error        ( 0x07, "Error" ),

    ;

    private final int code;

    private final String name;

    InstanceLifecycleEvent(int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
