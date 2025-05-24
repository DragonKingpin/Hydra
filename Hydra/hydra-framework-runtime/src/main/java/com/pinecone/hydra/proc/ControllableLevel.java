package com.pinecone.hydra.proc;

public enum ControllableLevel {
    None     ( 0x00, "None" ),

    Monitor  ( 0x01, "Monitor" ),

    Weak     ( 0x02, "Weak" ),

    Absolute ( 0x03, "Absolute" ),
    
    ;

    private final int code;

    private final String name;

    ControllableLevel( int code, String name ) {
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
