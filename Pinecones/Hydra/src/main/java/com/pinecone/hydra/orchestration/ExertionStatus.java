package com.pinecone.hydra.orchestration;

public enum ExertionStatus {
    NEW            ( "New"         ),
    RUNNING        ( "Running"     ),
    FINISHED       ( "Finished"    ), // Finished termination.
    TERMINATED     ( "Terminated"  ), // Forced termination.
    ROLLING        ( "Rolling"     ),
    INTERRUPTED    ( "Interrupted" ),
    ERROR          ( "Error"       );

    private final String value;
    ExertionStatus( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }

    public static ExertionStatus queryStatus( String sz ) {
        return ExertionStatus.valueOf( sz.toUpperCase() );
    }

    public boolean isEnded() {
        return this == ExertionStatus.FINISHED || this == ExertionStatus.TERMINATED || this == ExertionStatus.ERROR;
    }
}
