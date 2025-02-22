package com.pinecone.hydra.umc.msg;

public enum Status {
    SwitchingProtocols      ( 101, "SwitchingProtocols"  ),

    // For messenger(a.k.a, `Client`) or recipient (a.k.a, `Server`)
    // The confirmed and successful session
    OK                      ( 200, "OK"                  ),

    // For messenger(a.k.a, `Client`) or recipient (a.k.a, `Server`)
    // BadRequest or BadResponse
    BadSession              ( 400, "BadSession"          ),
    Unauthorized            ( 401, "Unauthorized"        ),
    IllegalMessage          ( 402, "IllegalMessage"      ),
    Forbidden               ( 403, "Forbidden"           ),
    MappingNotFound         ( 404, "MappingNotFound"     ),


    InternalError           ( 500, "InternalError"       ),
    NotImplemented          ( 501, "NotImplemented"      ),
    BadGateway              ( 502, "BadGateway"          ),
    Unavailable             ( 503, "Unavailable"         ),
    GatewayTimeout          ( 504, "GatewayTimeout"      ),
    VersionNotSupported     ( 505, "VersionNotSupported" ),
    TooManyConnections      ( 506, "TooManyConnections"  );


    private final int value;

    private final String name;

    Status( int value, String name ){
        this.value = value;
        this.name  = name;
    }

    public String getName(){
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public short getShortValue() {
        return (short) this.value;
    }

    public static Status asValue( int val ) {
        for ( Status type : Status.values() ) {
            if ( type.getValue() == val ) {
                return type;
            }
        }
        throw new IllegalArgumentException( "Invalid status value: " + val );
    }
}
