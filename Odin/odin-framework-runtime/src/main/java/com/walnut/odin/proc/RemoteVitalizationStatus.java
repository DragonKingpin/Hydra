package com.walnut.odin.proc;

public enum RemoteVitalizationStatus {
    Vitalized        ( 0x00 ),
    NoImage          ( 0x01 ),
    Error            ( 0x02 ),
    AuthorityDenial  ( 0x03 ),
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
