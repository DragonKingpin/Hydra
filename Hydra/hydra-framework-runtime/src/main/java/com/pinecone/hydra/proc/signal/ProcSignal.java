package com.pinecone.hydra.proc.signal;

public enum ProcSignal implements Signal {
    SIGINT,
    SIGTERM,
    SIGKILL;

    public static ProcSignal parse( String signal ) {
        if ( signal == null || signal.trim().isEmpty() ) {
            return SIGTERM;
        }
        for ( ProcSignal value : ProcSignal.values() ) {
            if ( value.name().equalsIgnoreCase( signal.trim() ) ) {
                return value;
            }
        }
        throw new IllegalArgumentException( "Unsupported process signal: " + signal );
    }
}
