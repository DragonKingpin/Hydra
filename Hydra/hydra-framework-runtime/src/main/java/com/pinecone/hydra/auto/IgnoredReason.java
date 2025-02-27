package com.pinecone.hydra.auto;

public enum IgnoredReason {
    Exception    ( "Exception" ),
    Violation    ( "Violation" ),
    Overtime     ( "Overtime"  ),
    Interrupt    ( "Interrupt"  ),
    Abort        ( "Abort"     );

    private final String value;
    IgnoredReason( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }
}