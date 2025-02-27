package com.pinecone.hydra.orchestration;

public enum IntegrityLevel {
    // Absolute and successfully transaction executed.
    // If errors happened, it will stop the whole transaction graph.
    Strict          ("Strict"),

    // Irresponsibly invoked the transaction.
    // If errors happened, it will ignored all errors(Tracing warning), and keeps continuity for next transactions.
    Warning         ("Warning"),

    // Irresponsibly invoked the transaction.
    // If errors happened, it will ignored all errors(No warning), and keeps continuity for next transactions.
    Irresponsible   ("Irresponsible");

    private final String value;

    IntegrityLevel( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }

    public static String queryName( IntegrityLevel type ) {
        return type.getName();
    }

    public static IntegrityLevel queryIntegrityLevel( String sz ) {
        return IntegrityLevel.valueOf( sz );
    }

    public static final String ConfIntegrityLevelKey = "IntegrityLevel";
}
