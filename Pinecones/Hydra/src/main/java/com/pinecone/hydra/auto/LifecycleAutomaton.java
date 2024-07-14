package com.pinecone.hydra.auto;

public interface LifecycleAutomaton extends Automatron {
    long getMaxLifetimeMillis();

    LifecycleAutomaton setMaxLifetimeMillis( long maxLifetimeMillis );

    long getHeartbeatTimeoutMillis();

    LifecycleAutomaton setHeartbeatTimeoutMillis( long heartbeatTimeoutMillis );

    default LifecycleAutomaton sendHeartbeat() {
        this.command( KernelInstructation.HEARTBEAT );
        return this;
    }
}
