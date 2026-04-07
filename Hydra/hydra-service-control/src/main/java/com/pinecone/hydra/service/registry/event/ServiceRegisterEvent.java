package com.pinecone.hydra.service.registry.event;

public enum ServiceRegisterEvent {
    Created      ( 0x00, "Created" ),

    Registered   ( 0x01, "Registered" ),

    Deregistered ( 0x02, "Deregistered" ),

    Detached     ( 0x03, "Detached" ),

    ;

    private final int code;

    private final String name;

    ServiceRegisterEvent(int code, String name ) {
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
