package com.sauron.heist.heistron;

public class HeistExecutionException extends HeistException {

    public HeistExecutionException    () {
        super();
    }

    public HeistExecutionException    ( String message ) {
        super(message);
    }

    public HeistExecutionException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public HeistExecutionException    ( Throwable cause ) {
        super(cause);
    }

}
