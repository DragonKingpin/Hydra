package com.walnut.odin.dispatch;

public class TaskConsumeException extends TaskDispatchException {

    protected TaskLaunchContext evictionTask;

    public TaskConsumeException() {
        super();
    }

    public TaskConsumeException( String message ) {
        super(message);
    }

    public TaskConsumeException( String message, Throwable cause, TaskLaunchContext context ) {
        super(message, cause);
        this.evictionTask = context;
    }

    public TaskConsumeException( Throwable cause, TaskLaunchContext context ) {
        super(cause);
        this.evictionTask = context;
    }

    public TaskConsumeException( String message, Throwable cause ) {
        super(message, cause);
    }

    public TaskConsumeException( Throwable cause ) {
        super(cause);
    }

    public TaskConsumeException( String message, TaskLaunchContext context ) {
        super(message);
        this.evictionTask = context;
    }

    public TaskLaunchContext getEvictionTask() {
        return this.evictionTask;
    }

    public void setEvictionTask( TaskLaunchContext evictionTask ) {
        this.evictionTask = evictionTask;
    }
}