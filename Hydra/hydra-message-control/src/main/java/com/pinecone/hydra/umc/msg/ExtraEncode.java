package com.pinecone.hydra.umc.msg;

public enum ExtraEncode {
    Undefined        ( 0x01, "Undefined"        ),
    JSONString       ( 0x02, "JSONString"       ),
    UlfBJson         ( 0x03, "UlfBJson"         ), // Ulf Binary JSON
    Blob             ( 0x04, "Blob"             ), // Raw binary blob
    Iussum           ( 0x05, "Iussum"           ), // Reduced instruction
    Custom           ( 0xFF, "Custom"           );

    private final int value;

    private final String name;

    ExtraEncode( int value, String name ) {
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

    public static ExtraEncode asValue( int val ) {
        for ( ExtraEncode type : ExtraEncode.values() ) {
            if ( type.getValue() == val ) {
                return type;
            }
        }

        return ExtraEncode.Custom;
    }
}
