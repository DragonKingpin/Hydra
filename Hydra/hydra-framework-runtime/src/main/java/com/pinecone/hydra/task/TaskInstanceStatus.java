package com.pinecone.hydra.task;

public enum TaskInstanceStatus {
    New                        ( 0x1000, "WAIT"     , "New"               ), // Instance created, pending for starting and scheduling.
    DependencyWait             ( 0x1001, "WAIT"     , "DependencyWait"    ), // DAG dependency wait.
    ResourceWait               ( 0x1002, "WAIT"     , "ResourceWait"      ), // Waiting for resource allocation.
    Standby                    ( 0x1003, "WAIT"     , "Standby"           ), // Process spawned.

    Running                    ( 0x2000, "RUNNING"  , "Running"           ), // Running.
    Audit                      ( 0x2001, "RUNNING"  , "Audit"             ), // Auditing and checking. [e.g. DQC check, Event check, etc.]

    Suspended                  ( 0x3000, "SUSPENDED", "Suspended"         ), // Process suspended.

    Finished                   ( 0x4000, "SUCCESS"  , "Finished"          ), // Finished termination.
    Killed                     ( 0x5000, "KILLED"   , "Killed"            ), // Forced termination.
    Error                      ( 0x6000, "FAIL"     , "Error"             ), // Error termination.
    AuditFailed                ( 0x6001, "FAIL"     , "AuditFailed"       ), // Auditing failed. [e.g. DQC failed etc.]
    ;

    private final int code;
    private final String name;
    private final String group;

    TaskInstanceStatus( int code, String group, String name ) {
        this.code  = code;
        this.group = group;
        this.name  = name;
    }

    public String getName(){
        return this.name;
    }

    public int getCode() {
        return this.code;
    }

    public String getGroup() {
        return this.group;
    }

    public static TaskInstanceStatus getByName(String name ) {
        for ( TaskInstanceStatus status : TaskInstanceStatus.values() ) {
            if ( status.name.equals(name) ) {
                return status;
            }
        }

        return null;
    }
}
