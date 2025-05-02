package com.pinecone.hydra.task.marshal;

public enum KernelTaskScheduleType {
    Undefined  ( 0x00, "Undefined" ),
    Cycle      ( 0x01, "Cycle" ),
    Manual     ( 0x02, "Manual" );

    private final int code;

    private final String name;

    KernelTaskScheduleType( int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getCode() {
        return this.code;
    }

    public static KernelTaskScheduleType getByCode( int code ) {
        for ( KernelTaskScheduleType type : KernelTaskScheduleType.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }
}
