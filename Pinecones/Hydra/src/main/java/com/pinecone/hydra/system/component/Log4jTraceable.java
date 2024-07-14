package com.pinecone.hydra.system.component;

import com.pinecone.framework.system.prototype.Pinenut;
import org.slf4j.Logger;

public interface Log4jTraceable extends Pinenut {
    Logger getLogger();

    default Log4jTraceable infoLifecycle( String szWhat, String szStateOrExtra ) {
        this.getLogger().info( "[Lifecycle] [{}] <{}>", szWhat, szStateOrExtra );
        return this;
    }

    default Log4jTraceable infoLifecycle( String szStateOrExtra ) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return this.infoLifecycle( stackTraceElements[ 2 ].getMethodName(), szStateOrExtra );
    }

    default Log4jTraceable infoLifecycleDone( String szWhat ) {
        return this.infoLifecycle( szWhat, LogStatuses.StatusDone );
    }

    default Log4jTraceable infoLifecycleInitializationDone() {
        return this.infoLifecycle( this.className() + "::Constructor", LogStatuses.StatusDone );
    }

    default Log4jTraceable infoCriticalOperation( String szWhat, String szStateOrExtra ) {
        this.getLogger().info( "[CriticalOperation] [{}] <{}>", szWhat, szStateOrExtra );
        return this;
    }

    default Log4jTraceable warnSimple( String szStateOrExtra ) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        this.getLogger().warn( "[{}] <{}>", stackTraceElements[ 2 ].getMethodName(), szStateOrExtra );
        return this;
    }
}
