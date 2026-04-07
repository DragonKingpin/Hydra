package com.pinecone.hydra.task;

public enum InstanceEventType {

    TaskSubmit             ( "TaskSubmit"           ),
    TaskTimeReady          ( "TaskTimeReady"        ),
    CheckDependencyReady   ( "CheckDependencyReady" ),
    DepartureReady         ( "DepartureReady"       ), // 实例离港完成，进入工作节点，等待远端正式执行
    TaskRun                ( "TaskRun"              ), // 实例启动完成，进入工作节点，远端进程进入运行
    TaskExecSuccess        ( "TaskExecSuccess"      ),
    TaskExecFail           ( "TaskExecFail"         ),
    TaskSuccess            ( "TaskSuccess"          ),
    TaskFail               ( "TaskFail"             ),
    AuditPost              ( "AuditPost"            ),
    AuditSuccess           ( "AuditSuccess"         ),
    AuditFail              ( "AuditFail"            ),
    TaskKilled             ( "TaskKilled"           );

    private final String name;

    InstanceEventType( String name ) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static InstanceEventType valueOfName( String name ) {
        try {
            return InstanceEventType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
}
