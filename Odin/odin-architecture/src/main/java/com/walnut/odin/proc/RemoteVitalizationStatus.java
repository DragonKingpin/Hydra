package com.walnut.odin.proc;

public enum RemoteVitalizationStatus {
    New              ( 0x00 ),
    Vitalized        ( 0x01 ),
    NoImage          ( 0x02 ),
    Error            ( 0x03 ),
    AuthorityDenial  ( 0x04 ),
    ;

    private final int code;

    RemoteVitalizationStatus( int code ) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static RemoteVitalizationStatus getByCode( int code ) {
        for ( RemoteVitalizationStatus type : RemoteVitalizationStatus.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }
}
