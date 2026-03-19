package com.pinecone.hydra.task;

public enum InstanceEvent {

    TaskSubmit             ( "TaskSubmit"           ),
    TaskTimeReady          ( "TaskTimeReady"        ),
    CheckDependencyReady   ( "CheckDependencyReady" ),
    TaskRun                ( "TaskRun"              ),
    TaskExecSuccess        ( "TaskExecSuccess"      ),
    TaskExecFail           ( "TaskExecFail"         ),
    TaskSuccess            ( "TaskSuccess"          ),
    TaskFail               ( "TaskFail"             ),
    AuditPost              ( "AuditPost"            ),
    AuditSuccess           ( "AuditSuccess"         ),
    AuditFail              ( "AuditFail"            ),
    TaskKilled             ( "TaskKilled"           );

    private final String name;

    InstanceEvent( String name ) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static InstanceEvent valueOfName( String name ) {
        try {
            return InstanceEvent.valueOf(name);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
}
