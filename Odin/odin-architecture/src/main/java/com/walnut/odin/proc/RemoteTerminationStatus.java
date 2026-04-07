package com.walnut.odin.proc;

public enum RemoteTerminationStatus {
    // === Normal ===
    Expected              (0x00),    // Expected termination, 计划内正常结束
    Error                 (0x01),    // Unexpected termination, 程序内部异常、错误
    HostKilled            (0x02),    // Affiliated otter-host-process termination, 由Daemon执行宿主进程死刑（宿主JVM进程终止）
    InitFailure           (0x03),    // Initialization Failure, 初始化构造失败（尚未真正开始执行）


    // === Signal ===
    SignalInterrupted     (0x20),    // Voluntary Interrupted (SIGINT), 收到信号程序主动中断 （走线程信号中断）
    SignalApoptosis       (0x21),    // Voluntary Apoptosis (SIGAPOP), 收到信号程序主动凋亡 （走程序性死亡协议）
    SignalElimination     (0x22),    // Destination killed by kernel (SIGELIM), 中央内核终末强制终止 （内部进程强制杀死）


    // === Procedure ===
    Restarted             (0x50),    // Expected restart, 计划内重启（如热更新、配置变更）
    Transferred           (0x51),    // Expected transfer, 计划内进程置换（如负载均衡、资源调度）
    Preempted             (0x52),    // Expected preempt, 计划内被调度器抢占式终止（后期自动恢复）


    // === Internal Failure ===
    AuthFailure           (0x60),    // Authority failure, 鉴权失败，安全策略拒绝
    HeathyCheckFailed     (0x61),    // Heathy check failed, 健康检查失败被终止
    ResourceExhausted     (0x62),    // Resource exhausted, 资源耗尽
    MigrationAborted      (0x63),    // Migration aborted, 进程迁移过程中失败或终止


    // === Network Error ===
    NetUnreachableTimeout (0x80),    // Net RPC unreachable timeout, 失联超时状态


    // === Checkpoint / Tombstone ===
    Suspended             (0xC001),  // Suspended, 可恢复的挂起状态
    SuspendedAborted      (0xC002),  // Suspended Aborted, 挂起状态被取消
    FrozenSeal            (0xC003),  // Frozen and seal, 封印状态（进程终止并持久化挂起到磁盘）
    CheckpointCorrupted   (0xC004);  // Checkpoint corrupted, 检查点不可用，恢复失败

    ;

    private final int code;

    RemoteTerminationStatus( int code ) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static RemoteTerminationStatus getByCode( int code ) {
        for ( RemoteTerminationStatus type : RemoteTerminationStatus.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }
}