package com.pinecone.hydra.orchestration;

import com.pinecone.hydra.orchestration.regulation.NeglectRegulation;

public interface Transaction extends Exertion {
    void add( Exertion exertion );

    void addFirst( Exertion exertion );

    NeglectRegulation getSeqExceptionNeglector();

    void setSeqExceptionNeglector( NeglectRegulation neglector ) ;

    void registerExertionStartCallback( ExertionEventCallback callback );

    void registerExertionEndCallback( ExertionEventCallback callback );
}
