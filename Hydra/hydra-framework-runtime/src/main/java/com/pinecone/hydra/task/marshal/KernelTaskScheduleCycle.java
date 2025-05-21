package com.pinecone.hydra.task.marshal;

public enum KernelTaskScheduleCycle {
    Undefined      ( 0x00, "Undefined" ),
    Minute         ( 0x01, "Minute" ),
    Hour           ( 0x02, "Hour" ),
    Day            ( 0x03, "Day" ),
    Week           ( 0x04, "Week" ),
    Month          ( 0x05, "Month" )
    ;

    private final int code;

    private final String name;

    KernelTaskScheduleCycle( int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getCode() {
        return this.code;
    }

    public static KernelTaskScheduleCycle getByCode( int code ) {
        for ( KernelTaskScheduleCycle cycle : KernelTaskScheduleCycle.values() ) {
            if ( cycle.code == code ) {
                return cycle;
            }
        }

        return null;
    }
}
