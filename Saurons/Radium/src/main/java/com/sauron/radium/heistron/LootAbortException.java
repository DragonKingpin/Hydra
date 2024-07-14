package com.sauron.radium.heistron;

public class LootAbortException extends RuntimeException {
    public LootAbortException() {
        super();
    }

    public LootAbortException( String message ) {
        super( message );
    }

    public LootAbortException( String message, Throwable cause ) {
        super( message, cause );
    }

    @Override
    public String toString() {
        return "[object LootAbortException]";
    }

    public String prototypeName() {
        return "LootAbortException";
    }
}