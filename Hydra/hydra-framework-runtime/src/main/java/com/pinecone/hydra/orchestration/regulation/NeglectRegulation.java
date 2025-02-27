package com.pinecone.hydra.orchestration.regulation;

public interface NeglectRegulation extends Regulation {
    boolean isNeglectException( Exception e );

    void add( Class<?> stereotype );
}
