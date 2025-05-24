package com.pinecone.hydra.task;

public enum TaskInstanceStatus {
    New                        ( "New"               ),
    WaitingDependency          ( "WaitingDependency" ),
    Running                    ( "Running"           ),
    Suspended                  ( "Suspended"         ),
    Finished                   ( "Finished"          ), // Finished termination.
    Terminated                 ( "Terminated"        ), // Forced termination.
    Error                      ( "Error"             ), // Error termination.
    ;

    private final String value;

    TaskInstanceStatus( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }

    public static TaskInstanceStatus getByName( String name ) {
        for ( TaskInstanceStatus status : TaskInstanceStatus.values() ) {
            if ( status.value.equals(name) ) {
                return status;
            }
        }

        return null;
    }
}
