package com.pinecone.hydra.proc.event;

public enum ProcessEvent {
    Created    ( 0x00, "Created" ),

    Vitalized  ( 0x01, "Vitalized" ),

    Terminated ( 0x02, "Terminated" ),

    Error      ( 0x03, "Error" ),

    ;

    private final int code;

    private final String name;

    ProcessEvent( int code, String name ) {
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
