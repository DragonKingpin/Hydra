package com.sauron.radium.heistron;

public class LootRecoveredException extends RuntimeException {
    public LootRecoveredException() {
        super();
    }

    public LootRecoveredException( String message ) {
        super( message );
    }

    public LootRecoveredException( String message, Throwable cause ) {
        super( message, cause );
    }

    @Override
    public String toString() {
        return "[object LootRecoveredException]";
    }

    public String prototypeName() {
        return "LootRecoveredException";
    }
}
