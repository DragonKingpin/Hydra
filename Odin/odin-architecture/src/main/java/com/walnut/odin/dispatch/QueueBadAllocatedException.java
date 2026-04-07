package com.walnut.odin.dispatch;

public class QueueBadAllocatedException extends TaskDispatchException {

    public QueueBadAllocatedException() {
        super();
    }

    public QueueBadAllocatedException( String message ) {
        super(message);
    }

    public QueueBadAllocatedException( String message, Throwable cause ) {
        super(message, cause);
    }

    public QueueBadAllocatedException( Throwable cause ) {
        super(cause);
    }

}
