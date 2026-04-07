package com.pinecone.hydra.task;

public enum TaskInstanceExecState {

    Success   ( "Success"   ),
    Fail      ( "Fail"      ),
    Running   ( "Running"   ),
    Submitted ( "Submitted" ),
    Killed    ( "Killed"    ),
    ;

    private final String name;

    TaskInstanceExecState( String name ) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
