package com.pinecone.hydra.task;

public enum InstanceEventType {

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

    InstanceEventType(String name ) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static InstanceEventType valueOfName(String name ) {
        try {
            return InstanceEventType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
}
