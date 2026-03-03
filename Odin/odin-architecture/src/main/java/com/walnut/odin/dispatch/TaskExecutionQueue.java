package com.walnut.odin.dispatch;

public interface TaskExecutionQueue extends TaskQueueMeta {

    void applyCapacity( int capacity );

    void applyMaxCapacity( int maxCapacity );

    void applyMinCapacity( int minCapacity );

    void applyRuntimeInstanceCapacity( int capacity );

}
