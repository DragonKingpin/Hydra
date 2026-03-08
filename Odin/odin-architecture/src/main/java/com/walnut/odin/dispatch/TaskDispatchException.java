package com.walnut.odin.dispatch;

public class TaskDispatchException extends Exception {

    public TaskDispatchException() {
        super();
    }

    public TaskDispatchException(String message ) {
        super(message);
    }

    public TaskDispatchException(String message, Throwable cause ) {
        super(message, cause);
    }

    public TaskDispatchException(Throwable cause ) {
        super(cause);
    }

}