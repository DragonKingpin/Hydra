package com.pinecone.hydra.service.registry.constant;

public enum ServiceVitalizationStatus {
    New              ( 0x00 ),
    Vitalized        ( 0x01 ),
    Error            ( 0x02 ),
    Success          ( 0x03 );
    private final int code;

    ServiceVitalizationStatus( int code ) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ServiceVitalizationStatus getByCode( int code ) {
        for ( ServiceVitalizationStatus type : ServiceVitalizationStatus.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }
}
