package com.pinecone.hydra.umc.msg;

public enum UMCMethod {
    UNDEFINED ( 0x00, "Undefined" ),
    PUT       ( 0x01, "Put"       ),
    POST      ( 0x02, "Post"      );

    private final int value;

    private final String name;

    UMCMethod( int value, String name ){
        this.value = value;
        this.name  = name;
    }

    public String getName(){
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public byte getByteValue() {
        return (byte) this.value;
    }

}
