package com.sauron.radium.system;

public class MissionTerminateException extends RuntimeException {
    public MissionTerminateException() {
        super();
    }

    public MissionTerminateException( String message ) {
        super( message );
    }

    public MissionTerminateException( String message, Throwable cause ) {
        super( message, cause );
    }

    @Override
    public String toString() {
        return "[object MissionTerminateException]";
    }

    public String prototypeName() {
        return "MissionTerminateException";
    }
}
