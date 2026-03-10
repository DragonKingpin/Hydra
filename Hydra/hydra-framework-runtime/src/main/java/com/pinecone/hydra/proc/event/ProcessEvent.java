package com.pinecone.hydra.proc.event;

public enum ProcessEvent {
    Prepare    ( 0x00, "Prepare" ), // Prepare to start, no image loaded.

    Created    ( 0x01, "Created" ),

    Vitalized  ( 0x02, "Vitalized" ),

    Terminated ( 0x03, "Terminated" ),

    Error      ( 0x04, "Error" ),

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
